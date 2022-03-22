ALTER TABLE ${annosets.table} ADD CONSTRAINT `pk_@{annosets.table}` PRIMARY KEY (${annosets.annosetid});
ALTER TABLE ${annosets.table} ADD KEY `k_@{annosets.table}_@{annosets.frameid}` (${annosets.frameid});
ALTER TABLE ${annosets.table} ADD KEY `k_@{annosets.table}_@{annosets.luid}` (${annosets.luid});
