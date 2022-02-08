CREATE UNIQUE INDEX `uniq_@{classes_roles.table}_@{classes_roles.roleid}_@{classes_roles.classid}` ON ${classes_roles.table} (${classes_roles.roleid},${classes_roles.classid});
CREATE INDEX `k_@{classes_roles.table}_@{classes_roles.roleid}` ON ${classes_roles.table} (${classes_roles.roleid});
CREATE INDEX `k_@{classes_roles.table}_@{classes_roles.classid}` ON ${classes_roles.table} (${classes_roles.classid});