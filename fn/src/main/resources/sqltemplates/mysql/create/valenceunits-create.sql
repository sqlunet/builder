CREATE TABLE IF NOT EXISTS ${valenceunits.table} (
    ${valenceunits.vuid} INTEGER NOT NULL,
    ${valenceunits.ferid} INTEGER NOT NULL,
    ${valenceunits.ptid} INTEGER,
    ${valenceunits.gfid} INTEGER,
PRIMARY KEY (${valenceunits.vuid}) );

-- ALTER TABLE ${valenceunits.table} ADD COLUMN ${valenceunits.pt} VARCHAR(20) AFTER ${valenceunits.ptid};
-- ALTER TABLE ${valenceunits.table} ADD COLUMN ${valenceunits.gf} VARCHAR(10) AFTER ${valenceunits.gfid};
