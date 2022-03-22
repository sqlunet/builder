CREATE UNIQUE INDEX `pk_@{fes.table}` ON ${fes.table} (${fes.feid});
CREATE INDEX `k_@{fes.table}_@{fes.frameid}` ON ${fes.table} (${fes.frameid});
CREATE INDEX `k_@{fes.table}_@{fes.fetypeid}` ON ${fes.table} (${fes.fetypeid});
