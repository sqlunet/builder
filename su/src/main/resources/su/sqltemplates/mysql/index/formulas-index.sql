ALTER TABLE ${formulas.table} ADD CONSTRAINT `pk_@{formulas.table}_@{formulas.formulaid}` PRIMARY KEY (${formulas.formulaid});
ALTER TABLE ${formulas.table} ADD KEY `k_@{formulas.table}_@{formulas.formula}` (${formulas.formula}(128));
ALTER TABLE ${formulas.table} ADD KEY `k_@{formulas.table}_@{formulas.fileid}` (${formulas.fileid});
