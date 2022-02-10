ALTER TABLE ${framerelations.table} ADD CONSTRAINT `uk_@{framerelations.table}_@{framerelations.relation}` UNIQUE KEY (${framerelations.relation});
