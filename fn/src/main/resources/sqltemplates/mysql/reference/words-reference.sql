${words}.fk1=ALTER TABLE ${words.table} ADD CONSTRAINT fk_${words.table}_${wordid} FOREIGN KEY (${wordid}) REFERENCES ${word.table} (${wordid});
