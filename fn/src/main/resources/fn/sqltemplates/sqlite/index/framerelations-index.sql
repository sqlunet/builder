CREATE UNIQUE INDEX `pk_@{framerelations.table}` ON ${framerelations.table} (${framerelations.relationid});
CREATE UNIQUE INDEX `uk_@{framerelations.table}_@{framerelations.relation}` ON ${framerelations.table} (${framerelations.relation});
