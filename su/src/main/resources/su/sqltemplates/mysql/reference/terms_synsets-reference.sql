ALTER TABLE ${terms_synsets.table} ADD CONSTRAINT `fk_@{terms_synsets.table}_@{terms_synsets.termid}` FOREIGN KEY (${terms_synsets.termid}) REFERENCES ${terms.table} (${terms.termid});
-- ALTER TABLE ${terms_synsets.table} ADD CONSTRAINT `fk_@{terms_synsets.table}_@{terms_synsets.synsetid}` FOREIGN KEY (${terms_synsets.synsetid}) REFERENCES ${wnsynsets.table} (${wnsynsets.synsetid});
