${governors_annosets}.fk1=ALTER TABLE ${governors_annosets.table} ADD CONSTRAINT fk_${governors_annosets.table}_${governorid} FOREIGN KEY (${governorid}) REFERENCES ${governors.table} (${governorid});
${governors_annosets}.fk2=ALTER TABLE ${governors_annosets.table} ADD CONSTRAINT fk_${governors_annosets.table}_${annosetid} FOREIGN KEY (${annosetid}) REFERENCES ${annosets.table} (${annosetid});
