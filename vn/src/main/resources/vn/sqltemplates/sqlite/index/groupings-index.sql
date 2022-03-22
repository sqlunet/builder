CREATE UNIQUE INDEX `pk_@{groupings.table}` ON ${groupings.table} (${groupings.groupingid});
CREATE UNIQUE INDEX `uk_@{groupings.table}_@{groupings.grouping}` ON ${groupings.table} (${groupings.grouping});
