CREATE UNIQUE INDEX `unq_@{restrtypes.table}_@{restrtypes.restrval}_@{restrtypes.restr}` ON ${restrtypes.table} (${restrtypes.restrval},${restrtypes.restr},${restrtypes.issyn});

