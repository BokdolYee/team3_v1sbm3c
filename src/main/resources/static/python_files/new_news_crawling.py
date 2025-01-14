import cx_Oracle
import requests
from bs4 import BeautifulSoup
from datetime import datetime
from textblob import TextBlob  # 텍스트 분석 라이브러리

# Oracle DB 연결 정보
DB_USER = 'team3'
DB_PASSWORD = '69017000'
DB_DSN = '15.165.85.117:1521/xe'

# 본문 추출 함수
def extract_text(news_soup):
    possible_containers = [
        'div.article__content',
        'div.l-container',
        'section.zn-body-text',
        'div.pg-rail-tall__body',
        'article',
        'div.article-body'
    ]
    for container in possible_containers:
        text_container = news_soup.select_one(container)
        if text_container:
            paragraphs = text_container.find_all(['p', 'span'])
            return '\n'.join(p.get_text(strip=True) for p in paragraphs if p.get_text(strip=True))
    return None

# 텍스트 분석 및 요약 함수
def summarize_text(text):
    blob = TextBlob(text)
    sentences = blob.sentences
    # 상위 3개의 문장으로 요약 (이 예시는 간단한 문장 추출 방식)
    summary = ' '.join(str(sentence) for sentence in sentences[:3])  # 첫 3문장만 가져오기
    return summary

# CNN 크롤링 함수
def crawl_cnn_news(keyword=None, seen_urls=set()):
    base_url = "https://edition.cnn.com/world"
    headers = {"User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36"}
    response = requests.get(base_url, headers=headers)

    if response.status_code != 200:
        print(f"Failed to fetch CNN page. Status code: {response.status_code}")
        return []

    print("페이지 HTML 가져오기 성공!")
    soup = BeautifulSoup(response.text, 'html.parser')

    articles = soup.find_all('span', class_='container__headline-text')
    print(f"찾은 기사 개수: {len(articles)}")

    if not articles:
        print("기사 요소를 찾을 수 없습니다.")
        return []

    news_data = []

    for article in articles[:5]:  # 최근 뉴스 5개만 가져옴
        try:
            title = article.get_text(strip=True)
            link = article.find_parent('a')['href']
            url = f"https://edition.cnn.com{link}" if link.startswith('/') else link

            # 이미 크롤링한 뉴스라면 건너뛰기
            if url in seen_urls:
                print(f"이미 크롤링한 뉴스: {url}")
                continue
            seen_urls.add(url)

            # 특정 단어 필터링 (newscateVO.name을 키워드로 사용)
            if keyword and keyword.lower() not in title.lower():
                print(f"제목에 '{keyword}' 단어가 없어서 건너뜁니다: {title}")
                continue

            news_response = requests.get(url, headers=headers)
            if news_response.status_code != 200:
                print(f"Failed to fetch article page: {url}")
                continue

            news_soup = BeautifulSoup(news_response.text, 'html.parser')

            text = extract_text(news_soup)
            if not text:
                print(f"본문을 찾을 수 없는 기사: {url}")
                continue

            # 기사 분석 및 요약
            summary = summarize_text(text)

            # 현재 시간으로 설정
            public_date = datetime.now()

            print(f"기사 URL: {url}")
            print(f"본문 내용: {text[:200] if text else '본문 없음'}")  # 본문 일부 출력
            print(f"작성 날짜: {public_date}")
            print(f"요약: {summary}")

            news_data.append({
                'text': text,
                'url': url,
                'source': 'CNN',
                'public_date': public_date,
                'summary': summary
            })

        except Exception as e:
            print(f"Error processing article: {e}")

    return news_data

# Oracle DB 저장 함수
def save_to_oracle(news_data):
    connection = None
    try:
        connection = cx_Oracle.connect(DB_USER, DB_PASSWORD, DB_DSN)
        cursor = connection.cursor()

        insert_query = """
        INSERT INTO news (newsno, text, url, source, PUBLISH_DATE, summary)
        VALUES (news_seq.NEXTVAL, :text, :url, :source, :publish_date, :summary)
        """
        for news in news_data:
            # datetime 객체를 직접 사용
            cursor.execute(insert_query, {
                'text': news['text'],
                'url': news['url'],
                'source': news['source'],
                'publish_date': news['public_date'],  # datetime 객체로 넘기기
                'summary': news['summary']
            })
        connection.commit()

    except cx_Oracle.DatabaseError as e:
        print(f"Database error: {e}")
    finally:
        if connection:
            connection.close()

# 실행
if __name__ == "__main__":
    seen_urls = set()  # 크롤링한 URL을 추적
    keyword = "stock"  # 예시: 'newscateVO.name'이 'stock'이라고 가정
    news_data = crawl_cnn_news(keyword, seen_urls)

    if news_data:
        save_to_oracle(news_data)
        print(f"뉴스 데이터를 성공적으로 저장했습니다. 저장된 뉴스 개수: {len(news_data)}")
    else:
        print("새로운 기사가 없습니다.")
