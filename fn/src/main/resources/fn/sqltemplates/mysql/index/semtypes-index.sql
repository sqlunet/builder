ALTER TABLE ${semtypes.table} ADD CONSTRAINT `uk_@{semtypes.table}_@{semtypes.semtype}` UNIQUE KEY (${semtypes.semtype});
