ALTER TABLE ${files.table} ADD CONSTRAINT `pk_@{files.table}_@{files.fileid}` PRIMARY KEY (${files.fileid});
ALTER TABLE ${files.table} ADD CONSTRAINT `uk_@{files.table}_@{files.file}` UNIQUE KEY (${files.file});
