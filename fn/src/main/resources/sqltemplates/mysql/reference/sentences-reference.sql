${sentences}.fk1=ALTER TABLE ${sentences.table} ADD CONSTRAINT fk_${sentences.table}_${documentid} FOREIGN KEY (${documentid}) REFERENCES ${documents.table} (${documentid});
${sentences}.fk2=ALTER TABLE ${sentences.table} ADD CONSTRAINT fk_${sentences.table}_${corpusid} FOREIGN KEY (${corpusid}) REFERENCES ${corpuses.table} (${corpusid});
