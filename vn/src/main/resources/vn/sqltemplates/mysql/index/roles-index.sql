ALTER TABLE ${roles.table} ADD CONSTRAINT `uniq_@{roles.table}_@{roles.roletypeid}_@{roles.restrsid}` UNIQUE KEY (${roles.roletypeid},${roles.restrsid});
ALTER TABLE ${roles.table} ADD KEY `k_@{roles.table}_@{roles.roletypeid}` (${roles.roletypeid});
ALTER TABLE ${roles.table} ADD KEY `k_@{roles.table}_@{roles.restrsid}` (${roles.restrsid});
