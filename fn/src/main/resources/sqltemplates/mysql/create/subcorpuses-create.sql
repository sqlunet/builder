CREATE TABLE IF NOT EXISTS ${subcorpuses.table} (
    ${subcorpuses.subcorpusid} INTEGER NOT NULL,
    ${subcorpuses.luid} INTEGER,
    ${subcorpuses.subcorpus} VARCHAR(80),
PRIMARY KEY (${subcorpuses.subcorpusid}) );
