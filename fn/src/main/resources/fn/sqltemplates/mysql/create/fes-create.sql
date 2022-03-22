CREATE TABLE IF NOT EXISTS ${fes.table} (
    ${fes.feid} INTEGER NOT NULL,
    ${fes.frameid} INTEGER,
    ${fes.fetypeid} INTEGER DEFAULT NULL,
    ${fes.feabbrev} VARCHAR(24),
    ${fes.fedefinition} TEXT,
    ${fes.coretypeid} INTEGER DEFAULT NULL,
    ${fes.coreset} INTEGER DEFAULT NULL
    -- ${fes.fgcolor} VARCHAR(6),
    -- ${fes.bgcolor} VARCHAR(6),
    -- ${fes.cdate} VARCHAR(27),
    -- ${fes.cby} VARCHAR(5)
);
