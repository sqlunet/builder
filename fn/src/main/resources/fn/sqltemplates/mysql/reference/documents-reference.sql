ALTER TABLE ${documents.table} ADD CONSTRAINT `fk_@{documents.table}_@{documents.corpusid}` FOREIGN KEY (${documents.corpusid}) REFERENCES ${corpuses.table} (${corpuses.corpusid});
