ALTER TABLE ${groupings.table} ADD CONSTRAINT `uk_@{groupings.table}_@{groupings.grouping}` UNIQUE KEY (${groupings.grouping});
