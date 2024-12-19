from flask import Flask, request, jsonify
from openai import OpenAI
import os

app = Flask(__name__)

# OpenAI 클라이언트 설정
client = OpenAI(api_key=os.getenv("OPENAI_API_KEY"))

# 카테고리와 주식 목록 정의
categories = {
    "경제(금융)": ["GS 골드만삭스", "JPM JP모건", "BAC 뱅크오브아메리카"],
    "IT": ["NVDA 엔비디아 코퍼레이션", "AMD AMD", "알파벳 A GOOGL"],
    "스포츠": ["Madison Square Garden Sports Corp MSGS", "TKO Group Holdings Inc TKO", "Liberty Media Formula One Corp A FWONA"],
    "문화": ["월트디즈니 DIS", "NETFLIX NFLX", "컴캐스트 CMCSA"],
    "정치": ["테슬라 TSLA", "DJ Transportation DJT", "코인베이스 글로벌 COIN"]
}

# 호재/악재 분석 함수
def analyze_text(text):
    # 카테고리와 주식 목록을 프롬프트에 포함
    category_info = "\n".join([f"{category}: {', '.join(stocks)}" for category, stocks in categories.items()])
    
    # OpenAI GPT API 호출
    response = client.chat.completions.create(
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
    
    return response.choices[0].message.content.strip()

# 분석 API 엔드포인트
@app.route('/analyze', methods=['POST'])
def analyze():
    data = request.get_json()  # 클라이언트에서 보낸 JSON 데이터 받기
    text = data.get('text', '')  # 뉴스 본문 받기
    
    if not text:
        return jsonify({"error": "No text provided"}), 400

    # 호재/악재 분석 수행
    analysis = analyze_text(text)
    
    response = jsonify({"analysis": analysis})
    response.headers.add('Content-Type', 'application/json; charset=utf-8')
    return response

if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=5001)
