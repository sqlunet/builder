CREATE UNIQUE INDEX `pk_@{restrs.table}` ON ${restrs.table} (${restrs.restrsid});
CREATE UNIQUE INDEX `uk_@{restrs.table}_@{restrs.restrs}` ON ${restrs.table} (${restrs.restrs},${restrs.issyn});
