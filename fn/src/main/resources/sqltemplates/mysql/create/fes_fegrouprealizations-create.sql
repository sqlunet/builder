CREATE TABLE IF NOT EXISTS ${fes_fegrouprealizations.table} (
    ${fes_fegrouprealizations.feid} INTEGER DEFAULT NULL,
    ${fes_fegrouprealizations.fetypeid} INTEGER DEFAULT NULL,
    ${fes_fegrouprealizations.fegrid} INTEGER NOT NULL,
PRIMARY KEY (${fes_fegrouprealizations.feid},${fes_fegrouprealizations.fetypeid},${fes_fegrouprealizations.fegrid}) );
