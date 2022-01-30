ALTER TABLE ${patterns.table} ADD CONSTRAINT fk_${patterns.table}_${patterns.fegrid} FOREIGN KEY (${patterns.fegrid}) REFERENCES ${fegrouprealizations.table} (${fegrouprealizations.fegrid});
