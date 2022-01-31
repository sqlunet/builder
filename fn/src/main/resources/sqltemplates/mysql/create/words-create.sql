CREATE TABLE IF NOT EXISTS ${fnwords.table} (
    ${fnwords.fnwordid} INTEGER NOT NULL,
    ${fnwords.wordid} INTEGER NULL,
    ${fnwords.word} VARCHAR(30),
PRIMARY KEY (${fnwords.fnwordid}) );
