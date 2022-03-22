ALTER TABLE ${lexunits.table} ADD CONSTRAINT `pk_@{lexunits.table}` PRIMARY KEY (${lexunits.luid});
ALTER TABLE ${lexunits.table} ADD KEY `k_@{lexunits.table}_@{lexunits.frameid}` (${lexunits.frameid});
