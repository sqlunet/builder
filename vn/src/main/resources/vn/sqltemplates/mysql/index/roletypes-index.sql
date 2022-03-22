ALTER TABLE ${roletypes.table} ADD CONSTRAINT `pk_@{roletypes.table}` PRIMARY KEY (${roletypes.roletypeid});
ALTER TABLE ${roletypes.table} ADD CONSTRAINT `uk_@{roletypes.table}_@{roletypes.roletype}` UNIQUE KEY (${roletypes.roletype});
