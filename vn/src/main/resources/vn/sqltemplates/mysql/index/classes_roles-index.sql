ALTER TABLE ${classes_roles.table} ADD CONSTRAINT `uniq_@{classes_roles.table}_@{classes_roles.roleid}_@{classes_roles.classid}` UNIQUE KEY (${classes_roles.roleid},${classes_roles.classid});
ALTER TABLE ${classes_roles.table} ADD KEY `k_@{classes_roles.table}_@{classes_roles.roleid}` (${classes_roles.roleid});
ALTER TABLE ${classes_roles.table} ADD KEY `k_@{classes_roles.table}_@{classes_roles.classid}` (${classes_roles.classid});
