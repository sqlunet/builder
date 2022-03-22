CREATE UNIQUE INDEX `pk_@{pttypes.table}` ON ${pttypes.table} (${pttypes.ptid});
CREATE UNIQUE INDEX `uk_@{pttypes.table}_@{pttypes.pt}` ON ${pttypes.table} (${pttypes.pt});
