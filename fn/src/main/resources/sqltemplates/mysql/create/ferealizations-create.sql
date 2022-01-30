CREATE TABLE IF NOT EXISTS ${ferealizations.table} (
    ${ferealizations.ferid} INTEGER NOT NULL,
    ${ferealizations.luid} INTEGER,
    ${ferealizations.fetypeid} INTEGER DEFAULT NULL,
    ${ferealizations.feid} INTEGER DEFAULT NULL,
    ${ferealizations.total} INTEGER,
PRIMARY KEY (${ferealizations.ferid}) );

-- ALTER TABLE ${ferealizations.table} ADD COLUMN ${ferealizations.fetype} VARCHAR(30) AFTER ${ferealizations.fetypeid};
