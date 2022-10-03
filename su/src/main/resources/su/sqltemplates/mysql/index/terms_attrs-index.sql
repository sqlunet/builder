ALTER TABLE ${terms_attrs.table} ADD CONSTRAINT `pk_@{terms_attrs.table}_@{terms_attrs.termid}_@{terms_attrs.attr}` PRIMARY KEY (${terms_attrs.termid},${terms_attrs.attr});
