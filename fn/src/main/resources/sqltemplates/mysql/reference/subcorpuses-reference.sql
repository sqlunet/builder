${subcorpuses}.fk1=ALTER TABLE ${subcorpuses.table} ADD CONSTRAINT fk_${subcorpuses.table}_${luid} FOREIGN KEY (${luid}) REFERENCES ${lexunits.table} (${luid});
