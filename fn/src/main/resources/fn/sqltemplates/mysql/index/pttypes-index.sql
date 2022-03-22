ALTER TABLE ${pttypes.table} ADD CONSTRAINT `pk_@{pttypes.table}` PRIMARY KEY (${pttypes.ptid});
ALTER TABLE ${pttypes.table} ADD CONSTRAINT `uk_@{pttypes.table}_@{pttypes.pt}` UNIQUE KEY (${pttypes.pt});
