ALTER TABLE ${subcorpuses.table} ADD CONSTRAINT `fk_@{subcorpuses.table}_@{subcorpuses.luid}` FOREIGN KEY (${subcorpuses.luid}) REFERENCES ${lexunits.table} (${lexunits.luid});
