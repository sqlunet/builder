ALTER TABLE ${terms_synsets.table} ADD CONSTRAINT `pk_@{terms_synsets.table}` PRIMARY KEY (${terms_synsets.termid},${terms_synsets.synsetid});
ALTER TABLE ${terms_synsets.table} ADD KEY `k_@{terms_synsets.table}_@{terms_synsets.synsetid}` (${terms_synsets.synsetid});
ALTER TABLE ${terms_synsets.table} ADD KEY `k_@{terms_synsets.table}_@{terms_synsets.termid}` (${terms_synsets.termid});
ALTER TABLE ${terms_synsets.table} ADD KEY `k_@{terms_synsets.table}_@{terms_synsets.rel}` (${terms_synsets.rel});
