CREATE TABLE IF NOT EXISTS ${documents.table} (
    ${documents.documentid} INTEGER NOT NULL,
    ${documents.document} VARCHAR(64),
    ${documents.documentdesc} VARCHAR(84),
    ${corpusid} INTEGER DEFAULT NULL,
    -- ${documents.noccurs} INTEGER DEFAULT 1,
PRIMARY KEY (${documents.documentid}) );
