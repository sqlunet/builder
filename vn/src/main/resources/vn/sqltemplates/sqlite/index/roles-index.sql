CREATE UNIQUE INDEX `pk_@{roles.table}` ON ${roles.table} (${roles.roleid});
CREATE UNIQUE INDEX `uk_@{roles.table}_@{roles.classid}_@{roles.roletypeid}}` ON ${roles.table} (${roles.classid},${roles.roletypeid});
CREATE INDEX `k_@{roles.table}_@{roles.classid}` ON ${roles.table} (${roles.classid});
CREATE INDEX `k_@{roles.table}_@{roles.roletypeid}` ON ${roles.table} (${roles.roletypeid});
