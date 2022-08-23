CREATE TABLE ${bncs.table} (
    ${bncs.wordid} INTEGER DEFAULT NULL,
    ${bncs.word} VARCHAR (80) CHARACTER SET utf8 COLLATE utf8mb4_0900_as_cs NOT NULL,
    ${bncs.posid} ENUM ('n','v','a','r','s') NOT NULL,
    ${bncs.freq} INTEGER DEFAULT NULL,
    ${bncs.range} INTEGER DEFAULT NULL,
    ${bncs.disp} FLOAT DEFAULT NULL
);
