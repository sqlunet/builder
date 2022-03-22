ALTER TABLE ${fes.table} ADD CONSTRAINT `pk_@{fes.table}` PRIMARY KEY (${fes.feid});
ALTER TABLE ${fes.table} ADD KEY `k_@{fes.table}_@{fes.frameid}` (${fes.frameid});
ALTER TABLE ${fes.table} ADD KEY `k_@{fes.table}_@{fes.fetypeid}` (${fes.fetypeid});
