ALTER TABLE ${rels.table} ADD CONSTRAINT `pk_@{rels.table}` PRIMARY KEY (${rels.relid});
ALTER TABLE ${rels.table} ADD KEY `k_@{rels.table}_@{rels.rel}` (${rels.rel});
