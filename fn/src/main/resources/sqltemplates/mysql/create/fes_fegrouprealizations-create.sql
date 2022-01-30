CREATE TABLE IF NOT EXISTS ${fes_fegrouprealizations.table} (
    ${fes_fegrouprealizations.rfeid} INTEGER,
    ${fes_fegrouprealizations.fegrid} INTEGER NOT NULL,
    ${fes_fegrouprealizations.feid} INTEGER DEFAULT NULL,
    ${fes_fegrouprealizations.fetypeid} INTEGER DEFAULT NULL,
PRIMARY KEY (${fes_fegrouprealizations.rfeid}) );

-- ALTER TABLE ${fes_fegrouprealizations.table} ADD COLUMN ${fes_fegrouprealizations.fetype} VARCHAR(30) AFTER ${fes_fegrouprealizations.fetypeid};
