CREATE UNIQUE INDEX `pk_@{annosets.table}` ON ${annosets.table} (${annosets.annosetid});
CREATE INDEX `k_@{annosets.table}_@{annosets.frameid}` ON ${annosets.table} (${annosets.frameid});
CREATE INDEX `k_@{annosets.table}_@{annosets.luid}` ON ${annosets.table} (${annosets.luid});
