CREATE UNIQUE INDEX `pk_@{roles.table}` ON ${roles.table} (${roles.roleid});
CREATE INDEX `k_@{roles.table}_@{roles.roledescr}` ON ${roles.table} (${roles.roledescr});
CREATE INDEX `k_@{roles.table}_@{roles.rolesetid}` ON ${roles.table} (${roles.rolesetid});
