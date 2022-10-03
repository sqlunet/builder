ALTER TABLE ${terms.table} ADD CONSTRAINT `pk_@{terms.table}_@{terms.termid}` PRIMARY KEY (${terms.termid});
ALTER TABLE ${terms.table} ADD CONSTRAINT `uk_@{terms.table}_@{terms.term}` UNIQUE KEY (${terms.term});
