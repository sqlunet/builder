CREATE UNIQUE INDEX `pk_@{lexunits.table}` ON ${lexunits.table} (${lexunits.luid});
CREATE INDEX `k_@{lexunits.table}_@{lexunits.frameid}` ON ${lexunits.table} (${lexunits.frameid});
