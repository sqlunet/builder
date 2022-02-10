CREATE TABLE IF NOT EXISTS ${documents.table} (
    ${documents.documentid} INTEGER NOT NULL,
    ${documents.document} VARCHAR(85),
    ${documents.documentdesc} VARCHAR(85),
    ${corpusid} INTEGER DEFAULT NULL,
    -- ${documents.noccurs} INTEGER DEFAULT 1,
PRIMARY KEY (${documents.documentid}) );
