CREATE UNIQUE INDEX `uniq_@{restrs.table}_@{restrs.restrs}` ON ${restrs.table} (${restrs.restrs},${restrs.issyn});
