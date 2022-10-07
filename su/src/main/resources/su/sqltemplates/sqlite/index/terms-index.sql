CREATE UNIQUE INDEX `pk_@{terms.table}_@{terms.termid}` ON ${terms.table} (${terms.termid});
CREATE UNIQUE INDEX `uk_@{terms.table}_@{terms.term}` ON ${terms.table} (${terms.term});
CREATE INDEX `k_@{terms.table}_@{terms.wordid}` ON ${terms.table} (${terms.wordid});
