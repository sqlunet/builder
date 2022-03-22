ALTER TABLE ${syntaxes.table} ADD CONSTRAINT `pk_@{syntaxes.table}` PRIMARY KEY (${syntaxes.syntaxid});
ALTER TABLE ${syntaxes.table} ADD CONSTRAINT `uk_@{syntaxes.table}_@{syntaxes.syntax}` UNIQUE KEY (${syntaxes.syntax}(80));
