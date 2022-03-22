ALTER TABLE ${framerelations.table} ADD CONSTRAINT `pk_@{framerelations.table}` PRIMARY KEY (${framerelations.relationid});
ALTER TABLE ${framerelations.table} ADD CONSTRAINT `uk_@{framerelations.table}_@{framerelations.relation}` UNIQUE KEY (${framerelations.relation});
