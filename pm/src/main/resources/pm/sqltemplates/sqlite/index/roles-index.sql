CREATE UNIQUE INDEX `pk_@{roles.table}` ON ${roles.table} (${roles.roleid});
CREATE INDEX `k_@{roles.table}_@{roles.predicateid}` ON ${roles.table} (${roles.predicateid});
