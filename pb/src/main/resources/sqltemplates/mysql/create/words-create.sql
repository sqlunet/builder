CREATE TABLE IF NOT EXISTS ${pbwords.table} (
    ${pbwords.pbwordid} INTEGER NOT NULL,
    ${pbwords.wordid} INTEGER NULL,
    ${pbwords.word} VARCHAR(80) DEFAULT NULL,
PRIMARY KEY (${pbwords.pbwordid}));
