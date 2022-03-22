ALTER TABLE ${lexemes.table} ADD CONSTRAINT `pk_@{lexemes.table}` PRIMARY KEY (${lexemes.lexemeid});
ALTER TABLE ${lexemes.table} ADD KEY `k_@{lexemes.table}_@{lexemes.luid}` (${lexemes.luid});
ALTER TABLE ${lexemes.table} ADD KEY `k_@{lexemes.table}_@{lexemes.fnwordid}` (${lexemes.fnwordid});
