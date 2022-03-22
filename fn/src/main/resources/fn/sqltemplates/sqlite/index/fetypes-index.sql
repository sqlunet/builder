CREATE UNIQUE INDEX `pk_@{fetypes.table}` ON ${fetypes.table} (${fetypes.fetypeid});
CREATE UNIQUE INDEX `uk_@{fetypes.table}_@{fetypes.fetype}` ON ${fetypes.table} (${fetypes.fetype});
