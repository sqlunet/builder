ALTER TABLE ${semtypes.table} ADD CONSTRAINT `pk_@{semtypes.table}` PRIMARY KEY (${semtypes.semtypeid});
ALTER TABLE ${semtypes.table} ADD CONSTRAINT `uk_@{semtypes.table}_@{semtypes.semtype}` UNIQUE KEY (${semtypes.semtype});
