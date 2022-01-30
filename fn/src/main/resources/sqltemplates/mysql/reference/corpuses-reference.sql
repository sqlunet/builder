CREATE TABLE IF NOT EXISTS ${corpuses.table} (
    ${corpuses.corpusid} INTEGER NOT NULL,
    ${corpuses.corpus} VARCHAR(80),
    ${corpuses.corpusdesc} VARCHAR(96),
    ${corpuses.luid} INTEGER DEFAULT NULL
    --${corpuses.noccurs} INTEGER DEFAULT 1,
PRIMARY KEY (${corpuses.corpusid}) );
