CREATE UNIQUE INDEX `pk_@{terms_senses.table}_@{terms_senses.synsetid}` ON ${terms_senses.table} (${terms_senses.synsetid});
CREATE INDEX `k_@{terms_senses.table}_@{terms_senses.termid}` ON ${terms_senses.table} (${terms_senses.termid});
CREATE INDEX `k_@{terms_senses.table}_@{terms_senses.rel}` ON ${terms_senses.table} (${terms_senses.rel});
