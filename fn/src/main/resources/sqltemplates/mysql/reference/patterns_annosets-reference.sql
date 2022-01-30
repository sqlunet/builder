${patterns_annosets}.fk1=ALTER TABLE ${patterns_annosets.table} ADD CONSTRAINT fk_${patterns_annosets.table}_${patternid} FOREIGN KEY (${patternid}) REFERENCES ${patterns.table} (${patternid});
${patterns_annosets}.fk2=ALTER TABLE ${patterns_annosets.table} ADD CONSTRAINT fk_${patterns_annosets.table}_${annosetid} FOREIGN KEY (${annosetid}) REFERENCES ${annosets.table} (${annosetid});
