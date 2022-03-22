CREATE UNIQUE INDEX `pk_@{coretypes.table}` ON ${coretypes.table} (${coretypes.coretypeid});
CREATE UNIQUE INDEX `uk_@{coretypes.table}_@{coretypes.coretype}` ON ${coretypes.table} (${coretypes.coretype});
