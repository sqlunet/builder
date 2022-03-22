CREATE UNIQUE INDEX `pk_@{syntaxes.table}` ON ${syntaxes.table} (${syntaxes.syntaxid});
CREATE UNIQUE INDEX `uk_@{syntaxes.table}_@{syntaxes.syntax}` ON ${syntaxes.table} (${syntaxes.syntax});
