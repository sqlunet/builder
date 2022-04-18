ALTER TABLE ${roles.table} ADD CONSTRAINT `pk_@{roles.table}` PRIMARY KEY (${roles.roleid});
ALTER TABLE ${roles.table} ADD KEY `k_@{roles.table}_@{roles.predicateid}` (${roles.predicateid});
