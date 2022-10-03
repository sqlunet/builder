CREATE UNIQUE INDEX `pk_@{formulas.table}_@{formulas.formulaid}` ON ${formulas.table} (${formulas.formulaid});
CREATE INDEX `k_@{formulas.table}_@{formulas.formula}` ON ${formulas.table} (${formulas.formula});
CREATE INDEX `k_@{formulas.table}_@{formulas.fileid}` ON ${formulas.table} (${formulas.fileid});
