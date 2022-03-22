ALTER TABLE ${roles.table} ADD CONSTRAINT `pk_@{roles.table}` PRIMARY KEY (${roles.roleid});
ALTER TABLE ${roles.table} ADD CONSTRAINT `uk_@{roles.table}_@{roles.classid}_@{roles.roletypeid}}` UNIQUE KEY (${roles.classid},${roles.roletypeid});
ALTER TABLE ${roles.table} ADD KEY `k_@{roles.table}_@{roles.classid}` (${roles.classid});
ALTER TABLE ${roles.table} ADD KEY `k_@{roles.table}_@{roles.roletypeid}` (${roles.roletypeid});
