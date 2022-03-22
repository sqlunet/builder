ALTER TABLE ${restrtypes.table} ADD CONSTRAINT `pk_@{restrtypes.table}` PRIMARY KEY (${restrtypes.restrid});
ALTER TABLE ${restrtypes.table} ADD CONSTRAINT `uk_@{restrtypes.table}_@{restrtypes.restrval}_@{restrtypes.restr}` UNIQUE KEY (${restrtypes.restrval},${restrtypes.restr},${restrtypes.issyn});
