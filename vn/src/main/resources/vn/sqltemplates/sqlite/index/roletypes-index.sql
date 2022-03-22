CREATE UNIQUE INDEX `pk_@{roletypes.table}` ON ${roletypes.table} (${roletypes.roletypeid});
CREATE UNIQUE INDEX `uk_@{roletypes.table}_@{roletypes.roletype}` ON ${roletypes.table} (${roletypes.roletype});
