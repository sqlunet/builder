${documents}.fk1=ALTER TABLE ${documents.table} ADD CONSTRAINT fk_${documents.table}_${corpusid} FOREIGN KEY (${corpusid}) REFERENCES ${corpuses.table} (${corpusid});
