CREATE UNIQUE INDEX `pk_@{rels.table}` ON ${rels.table} (${rels.relid});
CREATE INDEX `k_@{rels.table}_@{rels.rel}` ON ${rels.table} (${rels.rel});
