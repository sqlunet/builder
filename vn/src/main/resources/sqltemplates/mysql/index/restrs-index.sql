ALTER TABLE ${restrs.table} ADD CONSTRAINT `uniq_@{restrs.table}_@{restrs.restrs}` UNIQUE KEY (${restrs.restrs}(32),${restrs.issyn});
