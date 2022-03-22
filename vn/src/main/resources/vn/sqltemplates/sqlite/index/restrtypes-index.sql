CREATE UNIQUE INDEX `pk_@{restrtypes.table}` ON ${restrtypes.table} (${restrtypes.restrid});
CREATE UNIQUE INDEX `uk_@{restrtypes.table}_@{restrtypes.restrval}_@{restrtypes.restr}` ON ${restrtypes.table} (${restrtypes.restrval},${restrtypes.restr},${restrtypes.issyn});
