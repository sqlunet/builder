CREATE UNIQUE INDEX `pk_@{framenames.table}` ON ${framenames.table} (${framenames.framenameid});
CREATE UNIQUE INDEX `uk_@{framenames.table}_@{framenames.framename}` ON ${framenames.table} (${framenames.framename});
