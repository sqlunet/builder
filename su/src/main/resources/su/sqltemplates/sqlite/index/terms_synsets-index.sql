-- CREATE UNIQUE INDEX `pk_@{terms_synsets.table}` ON ${terms_synsets.table} (${terms_synsets.posid},${terms_synsets.synsetid});
CREATE INDEX `k_@{terms_synsets.table}_@{terms_synsets.termid}` ON ${terms_synsets.table} (${terms_synsets.termid});
CREATE INDEX `k_@{terms_synsets.table}_@{terms_synsets.maptype}` ON ${terms_synsets.table} (${terms_synsets.maptype});
CREATE INDEX `k_@{terms_synsets.table}_@{terms_synsets.synsetid}` ON ${terms_synsets.table} (${terms_synsets.synsetid});
