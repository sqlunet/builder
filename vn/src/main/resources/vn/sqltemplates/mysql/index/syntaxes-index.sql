ALTER TABLE ${syntaxes.table} ADD CONSTRAINT `uk_@{syntaxes.table}_@{syntaxes.syntax}` UNIQUE KEY (${syntaxes.syntax}(80));
