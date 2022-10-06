CREATE UNIQUE INDEX `pk_@{files.table}_@{files.fileid}` ON ${files.table} (${files.fileid});
CREATE UNIQUE INDEX `uk_@{files.table}_@{files.filename}` ON ${files.table} (${files.filename});
