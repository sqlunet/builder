ALTER TABLE ${roles.table} ADD CONSTRAINT `fk_@{roles.table}_@{roles.classid}` FOREIGN KEY (${roles.classid}) REFERENCES ${classes.table} (${classes.classid});
ALTER TABLE ${roles.table} ADD CONSTRAINT `fk_@{roles.table}_@{roles.roletypeid}` FOREIGN KEY (${roles.roletypeid}) REFERENCES ${roles.table} (${roles.roletypeid});
ALTER TABLE ${roles.table} ADD CONSTRAINT `fk_@{roles.table}_@{roles.restrsid}` FOREIGN KEY (${restrainedRoles.restrsid}) REFERENCES ${restrs.table} (${restrs.restrsid});
