DROP TABLE news CASCADE CONSTRAINTS; 
DROP TABLE news;

CREATE TABLE news(
    NEWSNO                            NUMBER(10)     NOT NULL    PRIMARY KEY,
    TEXT                             CLOB  NOT NULL,
    URL                              VARCHAR(255)    NULL,
    SOURCE                            VARCHAR(300)     NULL,
    PUBLISH_DATE                      DATE           NOT NULL
);

-- news 테이블의 외래 키 추가
ALTER TABLE news
ADD CONSTRAINT fk_news_analysis
FOREIGN KEY (ANALYSISNO) REFERENCES analysis (ANALYSISNO);

DROP SEQUENCE NEWS_SEQ

CREATE SEQUENCE NEWS_SEQ
START WITH 1         -- 시작 번호
INCREMENT BY 1       -- 증가값
MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
CACHE 2              -- 2번은 메모리에서만 계산
NOCYCLE;             -- 다시 1부터 생성되는 것을 방지

SELECT newsno, text, url, source, publish_date
FROM news
ORDER BY newsno DESC;

INSERT INTO news(newsno, TEXT, URL, SOURCE, PUBLISH_DATE) 
VALUES(news_seq.nextval, 'Manche ster City sinks further into crisis with defeat to Juventus in the Champions League By Matias Grez, CNN Published 8:15 AM EST, Thu December 12, 2024Manchester City’s ongoing crisis continues as Pep Guardiola’s side was beaten 2-0 away at Juventus in the Champions League. City has now won just once in its last 10 matches, suffering seven defeats. This is the worst run of Guardiola''s managerial career, surpassing the challenges faced during his tenure.Second-half goals from Dušan Vlahović and Weston McKennie secured Juventus a crucial win, leaving City in real danger of missing the knockout stages. Guardiola expressed frustration but optimism, stating, `We played good, but missed the last action. When we turn it around, we won’t forget this period.`Injuries to key players, including Manuel Akanji, John Stones, and Nathan Ake, have heavily impacted City’s defense, while Ballon d’Or winner Rodri’s absence has weakened the midfield. Despite having top players like Kevin De Bruyne and Erling Haaland, City has struggled with composure and control.City''s remaining group stage matches against PSG and Club Brugge are critical for advancing to the playoffs. Currently ranked 22nd in the new 36-team format, City risks missing the knockout rounds entirely.Barça edges thriller in DortmundBarcelona defeated Borussia Dortmund 3-2 in a dramatic match, securing its fifth consecutive Champions League win. Key moments included Raphinha’s opener and Ferrán Torres’ late winner, which came after Dortmund twice equalized.Saka stars for ArsenalBukayo Saka scored twice in Arsenal’s 3-0 win over Monaco, positioning the team for automatic qualification. Arsenal rebounded strongly after a draw with Fulham, with Kai Havertz adding the third goal late in the match.Wednesday’s results:Atlético Madrid 3-1 Slovan BratislavaLille 3-2 Sturm GrazBenfica 0-0 BolognaAC Milan 2-1 Red StarBorussia Dortmund 2-3 BarcelonaFeyenoord 4-2 Sparta PragueJuventus 2-0 Manchester CityStuttgart 5-1 Young BoysArsenal 3-0 Monaco', 'https://edition.cnn.com/2024/12/12/sport/manchester-city-sinks-crisis-champions-league-spt/index.html', 'CNN', sysdate);