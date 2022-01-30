CREATE TABLE IF NOT EXISTS ${annosets.table} (
    ${annosets.annosetid} INTEGER NOT NULL,
    ${annosets.sentenceid} INTEGER NOT NULL,
    ${annosets.frameid} INTEGER DEFAULT NULL,
    ${annosets.luid} INTEGER DEFAULT NULL,
    ${annosets.cxnid} INTEGER DEFAULT NULL,
    -- ${annosets.statusid} INTEGER DEFAULT NULL,
    -- ${annosets.cdate} VARCHAR(27),
    -- ${annosets.noccurs} INTEGER DEFAULT 1,
    PRIMARY KEY (${annosets.annosetid}) );
-- ALTER TABLE ${annosets.table} ADD COLUMN ${annosets.cxn} VARCHAR(32) AFTER ${annosets.cxnid};
-- ALTER TABLE ${annosets.table} ADD COLUMN ${annosets.status} VARCHAR(32) AFTER ${annosets.statusid};
