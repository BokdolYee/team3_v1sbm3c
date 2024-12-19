from flask import Flask, request, jsonify
from openai import OpenAI
import os

app = Flask(__name__)

# OpenAI 클라이언트 설정
client = OpenAI(api_key=os.getenv("OPENAI_API_KEY"))

# 텍스트 요약 함수
def summarize_text(text):
    response = client.chat.completions.create(
        model="gpt-4",  # 사용할 모델
        messages=[ 
            {"role": "system", "content": "너는 텍스트 요약을 전문으로 하는 AI야."},  # 시스템 역할 정의
            {"role": "user", "content": f"다음 텍스트를 3문장으로 요약해줘 문장들은 끝맺음이 확실해야해. 글자수 최대 300자인데 그게 넘어가지 않게 요약해:\n{text}"}  # 사용자 메시지
        ],
        max_tokens=300,  # 최대 토큰 수
        temperature=0.7  # 응답의 창의성 조절
    )
    
    # 'choices'를 사용하여 결과 추출 (message.content로 변경)
    return response.choices[0].message.content.strip()

# 요약 API 엔드포인트
@app.route('/summarize', methods=['POST'])
def summarize():
    data = request.get_json()  # 클라이언트에서 보낸 JSON 데이터 받기
    text = data.get('text', '')  # 뉴스 본문 받기
    
    if not text:
        return jsonify({"error": "No content provided"}), 400

    # 텍스트 요약 수행
    summary = summarize_text(text)
    
    # UTF-8 인코딩을 명시적으로 설정하여 응답을 반환
    response = jsonify({"summary": summary})
    response.headers.add('Content-Type', 'application/json; charset=utf-8')
    return response  # 요약 결과 반환

if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=5000)
