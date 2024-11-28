CREATE TABLE ${imaginfs.table} (
    ${imaginfs.wordid} INTEGER DEFAULT NULL,
    ${imaginfs.word} VARCHAR (80) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL,
    ${imaginfs.posid} ENUM ('n','v','a','r','s') NOT NULL,
    ${imaginfs.freq1} INTEGER DEFAULT NULL,
    ${imaginfs.range1} INTEGER DEFAULT NULL,
    ${imaginfs.disp1} FLOAT DEFAULT NULL,
    ${imaginfs.freq2} INTEGER DEFAULT NULL,
    ${imaginfs.range2} INTEGER DEFAULT NULL,
    ${imaginfs.disp2} FLOAT DEFAULT NULL,
    ${imaginfs.ll} FLOAT DEFAULT NULL
);
