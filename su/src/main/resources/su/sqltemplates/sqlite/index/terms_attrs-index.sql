CREATE UNIQUE INDEX `pk_@{terms_attrs.table}_@{terms_attrs.termid}_@{terms_attrs.attr}` ON ${terms_attrs.table} (${terms_attrs.termid},${terms_attrs.attr});
