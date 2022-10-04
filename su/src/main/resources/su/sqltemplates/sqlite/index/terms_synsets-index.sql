CREATE UNIQUE INDEX `pk_@{terms_synsets.table}` ON ${terms_synsets.table} (${terms_synsets.termid},${terms_synsets.synsetid});
CREATE INDEX `k_@{terms_synsets.table}_@{terms_synsets.synsetid}` ON ${terms_synsets.table} (${terms_synsets.synsetid});
CREATE INDEX `k_@{terms_synsets.table}_@{terms_synsets.termid}` ON ${terms_synsets.table} (${terms_synsets.termid});
CREATE INDEX `k_@{terms_synsets.table}_@{terms_synsets.rel}` ON ${terms_synsets.table} (${terms_synsets.rel});
