${governors}.fk1=ALTER TABLE ${governors.table} ADD CONSTRAINT fk_${governors.table}_${fnwordid} FOREIGN KEY (${fnwordid}) REFERENCES ${words.table} (${fnwordid});
