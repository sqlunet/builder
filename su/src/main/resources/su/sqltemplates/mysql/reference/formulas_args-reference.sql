ALTER TABLE ${formulas_args.table} ADD CONSTRAINT `fk_@{formulas_args.table}_@{formulas_args.formulaid}` FOREIGN KEY (${formulas_args.formulaid}) REFERENCES ${formulas.table} (${formulas.formulaid});
ALTER TABLE ${formulas_args.table} ADD CONSTRAINT `fk_@{formulas_args.table}_@{formulas_args.termid}` FOREIGN KEY (${formulas_args.termid}) REFERENCES ${terms.table} (${terms.termid});
