CREATE UNIQUE INDEX `pk_@{lexemes.table}` ON ${lexemes.table} (${lexemes.lexemeid});
CREATE INDEX `k_@{lexemes.table}_@{lexemes.luid}` ON ${lexemes.table} (${lexemes.luid});
CREATE INDEX `k_@{lexemes.table}_@{lexemes.fnwordid}` ON ${lexemes.table} (${lexemes.fnwordid});
