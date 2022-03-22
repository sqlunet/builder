ALTER TABLE ${fetypes.table} ADD CONSTRAINT `pk_@{fetypes.table}` PRIMARY KEY (${fetypes.fetypeid});
ALTER TABLE ${fetypes.table} ADD CONSTRAINT `uk_@{fetypes.table}_@{fetypes.fetype}` UNIQUE KEY (${fetypes.fetype});
