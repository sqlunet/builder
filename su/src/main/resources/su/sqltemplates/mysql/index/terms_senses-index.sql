ALTER TABLE ${terms_senses.table} ADD CONSTRAINT `pk_@{terms_senses.table}_@{terms_senses.synsetid}` PRIMARY KEY (${terms_senses.synsetid});
ALTER TABLE ${terms_senses.table} ADD KEY `k_@{terms_senses.table}_@{terms_senses.termid}` (${terms_senses.termid});
ALTER TABLE ${terms_senses.table} ADD KEY `k_@{terms_senses.table}_@{terms_senses.rel}` (${terms_senses.rel});
