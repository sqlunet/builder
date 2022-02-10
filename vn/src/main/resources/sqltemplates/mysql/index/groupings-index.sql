ALTER TABLE ${groupings.table} ADD CONSTRAINT `uniq_@{groupings.table}_@{groupings.grouping}` UNIQUE KEY (${groupings.grouping});
