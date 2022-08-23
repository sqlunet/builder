CREATE TABLE ${spwrs.table} (
    ${spwrs.wordid} INTEGER DEFAULT NULL,
    ${convtasks.word} VARCHAR (80) CHARACTER SET utf8 COLLATE utf8mb4_0900_as_cs NOT NULL,
    ${spwrs.posid} ENUM ('n','v','a','r','s') NOT NULL,
    ${spwrs.freq1} INTEGER DEFAULT NULL,
    ${spwrs.range1} INTEGER DEFAULT NULL,
    ${spwrs.disp1} FLOAT DEFAULT NULL,
    ${spwrs.freq2} INTEGER DEFAULT NULL,
    ${spwrs.range2} INTEGER DEFAULT NULL,
    ${spwrs.disp2} FLOAT DEFAULT NULL,
    ${spwrs.ll} FLOAT DEFAULT NULL
);
