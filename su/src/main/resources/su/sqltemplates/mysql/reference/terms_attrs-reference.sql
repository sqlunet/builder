ALTER TABLE ${terms_attrs.table} ADD CONSTRAINT `fk_@{terms_attrs.table}_@{terms_attrs.termid}` FOREIGN KEY (${terms_attrs.termid}) REFERENCES ${terms.table} (${terms.termid});
