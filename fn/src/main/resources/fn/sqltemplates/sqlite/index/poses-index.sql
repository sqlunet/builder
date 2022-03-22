CREATE UNIQUE INDEX `pk_@{poses.table}` ON ${poses.table} (${poses.posid});
CREATE UNIQUE INDEX `uk_@{poses.table}_@{poses.pos}` ON ${poses.table} (${poses.pos});
