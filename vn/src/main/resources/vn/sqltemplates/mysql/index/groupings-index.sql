ALTER TABLE ${groupings.table} ADD CONSTRAINT `pk_@{groupings.table}` PRIMARY KEY (${groupings.groupingid});
ALTER TABLE ${groupings.table} ADD CONSTRAINT `uk_@{groupings.table}_@{groupings.grouping}` UNIQUE KEY (${groupings.grouping});
