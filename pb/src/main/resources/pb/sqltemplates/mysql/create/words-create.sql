CREATE TABLE IF NOT EXISTS ${words.table} (
    ${words.pbwordid} INTEGER NOT NULL,
    ${words.wordid} INTEGER NULL,
    ${words.word} VARCHAR(80) CHARACTER SET utf8 COLLATE utf8mb4_0900_as_cs NOT NULL
);
