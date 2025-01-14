import cx_Oracle
import requests
from bs4 import BeautifulSoup
from datetime import datetime
from openai import OpenAI
import os
from flask import Flask, request, jsonify

app = Flask(__name__)

# OpenAI 클라이언트 설정
client = OpenAI(api_key=os.getenv("OPENAI_API_KEY"))

# Oracle DB 연결 정보
DB_USER = 'team3'
DB_PASSWORD = '69017000'
DB_DSN = '15.165.85.117:1521/xe'

# 카테고리와 주식 목록 정의
categories = {
    "경제(금융)": ["GS 골드만삭스", "JPM JP모건", "BAC 뱅크오브아메리카"],
    "IT": ["NVDA 엔비디아 코퍼레이션", "AMD AMD", "알파벳 A GOOGL"],
    "스포츠": ["Madison Square Garden Sports Corp MSGS", "TKO Group Holdings Inc TKO", "Liberty Media Formula One Corp A FWONA"],
    "문화": ["월트디즈니 DIS", "NETFLIX NFLX", "컴캐스트 CMCSA"],
    "정치": ["테슬라 TSLA", "DJ Transportation DJT", "코인베이스 글로벌 COIN"]
}

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
def analyze_and_summarize_text(text):
    # 분석
    category_info = "\n".join([f"{category}: {', '.join(stocks)}" for category, stocks in categories.items()])
    analysis_response = client.chat.completions.create(
        model="gpt-4",
        messages=[
            {
                "role": "system",
                "content": (
                    "너는 텍스트 분석을 전문으로 하는 AI야. 다음 뉴스가 주식 시장에 미치는 영향을 분석하고, "
                    "관련된 주식 카테고리를 기반으로 호재인지 악재인지 판단해줘. 주식 목록은 다음과 같아:\n"
                    + category_info +
                    "\n결과는 반드시 다음 형식으로 작성해:\n"
                    "1. [호재/악재]: [카테고리] [관련 주식]가 호재/악재로 예상됨.\n"
                    "2. 관련 없는 경우는 출력하지마."
                    "3. 최대한 한줄로 작성해."
                    "4. 만약 [카테고리] [관련 주식]이 아무것도 관련이 없을시 [데이터베이스에 있는 주식과는 관련이 없습니다.]라고 출력해"
                    "5. 반드시 300자 이내로만 작성해야해. 문장은 깔끔하고 간결하게 작성해야해."
                )
            },
            {
                "role": "user",
                "content": f"다음 텍스트를 분석해서 관련 카테고리와 주식의 호재/악재를 판단해줘:\n{text}"
            }
        ],
        max_tokens=300,
        temperature=0.7
    )
    
    analysis = analysis_response.choices[0].message.content.strip()

    # 요약
    summarize_response = client.chat.completions.create(
        model="gpt-4",  
        messages=[ 
            {"role": "system", "content": "너는 텍스트를 한국어로 요약을 전문으로 하는 AI야."},  
            {"role": "user", "content": f"다음 텍스트를 3문장으로 요약해줘 문장들은 끝맺음이 확실해야해. 글자수 최대 300자인데 그게 넘어가지 않게 요약해:\n{text}"}  
        ],
        max_tokens=300,  
        temperature=0.7  
    )
    
    summary = summarize_response.choices[0].message.content.strip()
    
    return analysis, summary

# CNN 크롤링 함수
def crawl_cnn_news():
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
    for article in articles[:5]:
        try:
            title = article.get_text(strip=True)
            link = article.find_parent('a')['href']
            url = f"https://edition.cnn.com{link}" if link.startswith('/') else link

            news_response = requests.get(url, headers=headers)
            if news_response.status_code != 200:
                print(f"Failed to fetch article page: {url}")
                continue

            news_soup = BeautifulSoup(news_response.text, 'html.parser')

            text = extract_text(news_soup)
            public_date = datetime.now()

            print(f"기사 URL: {url}")
            print(f"본문 내용: {text[:200] if text else '본문 없음'}")  # 본문 일부 출력
            print(f"작성 날짜: {public_date}")

            if text:
                # 분석 및 요약 수행
                analysis, summary = analyze_and_summarize_text(text)
                
                news_data.append({
                    'text': text,
                    'url': url,
                    'source': 'CNN',
                    'public_date': public_date,
                    'impact': analysis,  # impact 추가
                    'summary': summary   # summary 추가
                })
            else:
                print(f"본문을 찾을 수 없는 기사: {url}")
        except Exception as e:
            print(f"Error processing article: {e}")

    return news_data

# Oracle DB 저장 함수
def save_to_oracle(news_data):
    connection = None
    try:
        connection = cx_Oracle.connect(DB_USER, DB_PASSWORD, DB_DSN)
        cursor = connection.cursor()

        insert_query_news = """
        INSERT INTO news (newsno, text, url, source, PUBLISH_DATE, impact, summary)
        VALUES (news_seq.NEXTVAL, :text, :url, :source, :publish_date, :impact, :summary)
        """
        for news in news_data:
            cursor.execute(insert_query_news, {
                'text': news['text'],
                'url': news['url'],
                'source': news['source'],
                'publish_date': news['public_date'],
                'impact': news['impact'],
                'summary': news['summary']
            })
        
        connection.commit()

    except cx_Oracle.DatabaseError as e:
        print(f"Database error: {e}")
    finally:
        if connection:
            connection.close()

# 크롤링과 요약, 분석을 실행하는 API 엔드포인트
@app.route('/start_crawl', methods=['GET'])
def start_crawl():
    news_data = crawl_cnn_news()
    
    if not news_data:
        return jsonify({"error": "새로 추가된 뉴스가 없습니다."}), 400
    
    save_to_oracle(news_data)
    
    return jsonify({"message": "뉴스 크롤링, 분석 및 요약이 완료되었습니다.", "news_count": len(news_data)})

if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=5000)