ALTER TABLE ${coretypes.table} ADD CONSTRAINT `pk_@{coretypes.table}` PRIMARY KEY (${coretypes.coretypeid});
ALTER TABLE ${coretypes.table} ADD CONSTRAINT `uk_@{coretypes.table}_@{coretypes.coretype}` UNIQUE KEY (${coretypes.coretype});
