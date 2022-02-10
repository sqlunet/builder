ALTER TABLE ${annosets.table} ADD KEY `k_@{annosets.table}_@{annosets.frameid}` (${annosets.frameid});
ALTER TABLE ${annosets.table} ADD KEY `k_@{annosets.table}_@{annosets.luid}` (${annosets.luid});
