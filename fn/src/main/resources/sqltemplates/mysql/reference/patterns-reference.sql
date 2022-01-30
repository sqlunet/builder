${patterns}.fk1=ALTER TABLE ${patterns.table} ADD CONSTRAINT fk_${patterns.table}_${fegrid} FOREIGN KEY (${fegrid}) REFERENCES ${fegrouprealizations.table} (${fegrid});
