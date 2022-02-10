ALTER TABLE ${sentences.table} ADD CONSTRAINT `fk_@{sentences.table}_@{sentences.documentid}` FOREIGN KEY (${sentences.documentid}) REFERENCES ${documents.table} (${documents.documentid});
ALTER TABLE ${sentences.table} ADD CONSTRAINT `fk_@{sentences.table}_@{sentences.corpusid}` FOREIGN KEY (${sentences.corpusid}) REFERENCES ${corpuses.table} (${corpuses.corpusid});
