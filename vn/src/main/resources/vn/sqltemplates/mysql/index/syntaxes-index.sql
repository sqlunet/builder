ALTER TABLE ${syntaxes.table} ADD CONSTRAINT `uniq_@{syntaxes.table}_@{syntaxes.syntax}` UNIQUE KEY (${syntaxes.syntax}(80));
