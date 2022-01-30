CREATE TABLE IF NOT EXISTS ${documents.table} (
    ${documents.documentid} INTEGER NOT NULL,
    ${corpusid} INTEGER DEFAULT NULL,
    ${documents.documentdesc} VARCHAR(84),
    ${documents.noccurs} INTEGER DEFAULT 1,
PRIMARY KEY (${documents.documentid}) );
