ALTER TABLE ${framenames.table} ADD CONSTRAINT `pk_@{framenames.table}` PRIMARY KEY (${framenames.framenameid});
ALTER TABLE ${framenames.table} ADD CONSTRAINT `uk_@{framenames.table}_@{framenames.framename}` UNIQUE KEY (${framenames.framename});
