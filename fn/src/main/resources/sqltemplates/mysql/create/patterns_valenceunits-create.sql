CREATE TABLE IF NOT EXISTS ${patterns_valenceunits.table} (
    ${patterns_valenceunits.pvid} INTEGER NOT NULL,
    ${patterns_valenceunits.patternid} INTEGER NOT NULL,
    ${patterns_valenceunits.vuid} INTEGER NOT NULL,
    ${patterns_valenceunits.feid} INTEGER,
    ${patterns_valenceunits.fetypeid} INTEGER,
PRIMARY KEY (${patterns_valenceunits.pvid}) );

-- ALTER TABLE ${patterns_valenceunits.table} ADD COLUMN ${patterns_valenceunits.fetype} VARCHAR(30) AFTER ${patterns_valenceunits.feid};
