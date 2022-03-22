CREATE UNIQUE INDEX `pk_@{semtypes.table}` ON ${semtypes.table} (${semtypes.semtypeid});
CREATE UNIQUE INDEX `uk_@{semtypes.table}_@{semtypes.semtype}` ON ${semtypes.table} (${semtypes.semtype});
