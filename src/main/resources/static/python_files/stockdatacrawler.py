import yfinance as yf
import cx_Oracle
from datetime import datetime

# Oracle DB 연결
def get_db_connection():
    # Oracle DB 연결 정보 (본인의 정보에 맞게 수정)
    dsn_tns = cx_Oracle.makedsn('15.165.85.117', '1521', service_name='XE')  # 서비스 이름에 맞게 수정
    connection = cx_Oracle.connect(user='team3', password='69017000', dsn=dsn_tns)
    return connection

# 거래량을 인간이 읽기 쉬운 형식으로 변환
def format_volume(volume):
    if volume >= 1_000_000:
        return f"{volume / 1_000_000:.2f}M"
    elif volume >= 1_000:
        return f"{volume / 1_000:.2f}K"
    else:
        return str(volume)

# 주식 데이터를 가져오는 함수
def fetch_stock_data(symbol):
    stock = yf.Ticker(symbol)
    # 최근 5일간의 주식 데이터 (주기적으로 업데이트할 수 있도록 수정 가능)
    data = stock.history(period="5d")
    return data

# 주식 데이터를 DB에 업데이트하는 함수
def update_stock_data_in_db(symbol, stock_data):
    # DB 연결
    connection = get_db_connection()
    cursor = connection.cursor()

    for index, row in stock_data.iterrows():
        # rdate에 현재 시점의 정확한 날짜와 시간 삽입 (Python의 datetime.now() 사용)
        rdate = datetime.now()  # 현재 시스템 시간 (로컬 시간)

        open_price = round(row['Open'], 2)  # 시가 (소수점 2자리)
        close_price = round(row['Close'], 2)  # 종가 (소수점 2자리)
        volume = format_volume(row['Volume'])  # 거래량 (형식 변환)
        change = round(((row['Close'] - row['Open']) / row['Open']) * 100, 2)  # 변동률 (소수점 2자리)
        
        # 변동률 앞에 + 또는 - 기호 추가
        if change > 0:
            change = f"+{change}%"  # 양수일 경우 + 기호 추가
        else:
            change = f"{change}%"   # 음수일 경우 자동으로 - 기호가 붙음

        # SQL 쿼리 작성 (symbol을 기준으로 업데이트, rdate는 SYSDATE 사용)
        sql = """
            UPDATE Stockdata
            SET open_price = :open_price, close_price = :close_price, volume = :volume, change = :change, rdate = SYSDATE
            WHERE symbol = :symbol
        """
        # 쿼리 실행
        cursor.execute(sql, open_price=open_price, close_price=close_price,
                       volume=volume, change=change, symbol=symbol)

        # 업데이트된 행 수 출력
        print(f"Rows updated: {cursor.rowcount}")

    # DB 커밋 및 연결 종료
    connection.commit()
    cursor.close()
    connection.close()

# 주식 데이터를 크롤링하여 DB에 업데이트하는 메인 함수
def main():
    # DB 연결
    connection = get_db_connection()
    cursor = connection.cursor()

    # 모든 symbol 값을 가져옴
    cursor.execute("SELECT DISTINCT symbol FROM Stockdata")
    symbols = cursor.fetchall()

    # 각 symbol에 대해 데이터 크롤링 및 업데이트 수행
    for symbol in symbols:
        symbol = symbol[0]  # 튜플에서 첫 번째 값만 추출
        stock_data = fetch_stock_data(symbol)
        update_stock_data_in_db(symbol, stock_data)
        print(f"Stock data for {symbol} has been updated in the database.")

    # 연결 종료
    cursor.close()
    connection.close()

if __name__ == "__main__":
    main()
