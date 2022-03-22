ALTER TABLE ${restrs.table} ADD CONSTRAINT `pk_@{restrs.table}` PRIMARY KEY (${restrs.restrsid});
ALTER TABLE ${restrs.table} ADD CONSTRAINT `uk_@{restrs.table}_@{restrs.restrs}` UNIQUE KEY (${restrs.restrs}(32),${restrs.issyn});
