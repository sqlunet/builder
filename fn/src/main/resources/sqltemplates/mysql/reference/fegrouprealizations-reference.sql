${fegrouprealizations}.fk1=ALTER TABLE ${fegrouprealizations.table} ADD CONSTRAINT fk_${fegrouprealizations.table}_${luid} FOREIGN KEY (${luid}) REFERENCES ${lexunits.table} (${luid});
