ALTER TABLE ${framenames.table} ADD CONSTRAINT `uniq_@{framenames.table}_@{framenames.framename}` UNIQUE KEY (${framenames.framename});
