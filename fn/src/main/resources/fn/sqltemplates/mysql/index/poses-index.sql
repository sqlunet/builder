ALTER TABLE ${poses.table} ADD CONSTRAINT `uk_@{poses.table}_@{poses.pos}` UNIQUE KEY (${poses.pos});
