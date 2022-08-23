CREATE TABLE ${syntagms.table} (
    ${syntagms.syntagmid} INTEGER NOT NULL,
    ${syntagms.word1id} INTEGER NULL,
    ${syntagms.synset1id} INTEGER NULL,
    ${syntagms.word2id} INTEGER NULL,
    ${syntagms.synset2id} INTEGER NULL,
    ${syntagms.sensekey1} VARCHAR (100) CHARACTER SET utf8 COLLATE utf8mb4_0900_as_cs NOT NULL,
    ${syntagms.sensekey2} VARCHAR (100) CHARACTER SET utf8 COLLATE utf8mb4_0900_as_cs NOT NULL
);
