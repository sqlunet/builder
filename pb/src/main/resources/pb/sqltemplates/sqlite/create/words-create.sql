CREATE TABLE IF NOT EXISTS ${words.table} (
${words.pbwordid} INTEGER NOT NULL,
${words.wordid} INTEGER NULL,
${words.word} VARCHAR(80) NOT NULL,
PRIMARY KEY (${words.pbwordid}));
