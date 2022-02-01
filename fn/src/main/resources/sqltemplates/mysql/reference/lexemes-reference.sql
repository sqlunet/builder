ALTER TABLE ${lexemes.table} ADD CONSTRAINT `fk_@{lexemes.table}_@{lexemes.luid}` FOREIGN KEY (${lexemes.luid}) REFERENCES ${lexunits.table} (${lexunits.luid});
ALTER TABLE ${lexemes.table} ADD CONSTRAINT `fk_@{lexemes.table}_@{lexemes.posid}` FOREIGN KEY (${lexemes.posid}) REFERENCES ${poses.table} (${poses.posid});
ALTER TABLE ${lexemes.table} ADD CONSTRAINT `fk_@{lexemes.table}_@{lexemes.fnwordid}` FOREIGN KEY (${lexemes.fnwordid}) REFERENCES ${fnwords.table} (${fnwords.fnwordid});
