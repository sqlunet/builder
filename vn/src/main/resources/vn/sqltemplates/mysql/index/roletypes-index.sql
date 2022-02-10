ALTER TABLE ${roletypes.table} ADD CONSTRAINT `uniq_@{roletypes.table}_@{roletypes.roletype}` UNIQUE KEY (${roletypes.roletype});
