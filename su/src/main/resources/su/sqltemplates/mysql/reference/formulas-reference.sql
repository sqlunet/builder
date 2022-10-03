ALTER TABLE ${formulas.table} ADD CONSTRAINT `fk_@{formulas.table}_@{formulas.fileid}` FOREIGN KEY (${formulas.fileid}) REFERENCES ${files.table} (${files.fileid});
