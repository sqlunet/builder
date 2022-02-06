ALTER TABLE ${classes_roles.table} ADD CONSTRAINT fk_${classes_roles.table}_${classes_roles.roleid} FOREIGN KEY (${classes_roles.roleid}) REFERENCES ${roles.table} (${roles.roleid});
ALTER TABLE ${classes_roles.table} ADD CONSTRAINT fk_${classes_roles.table}_${classes_roles.classid} FOREIGN KEY (${classes_roles.classid}) REFERENCES ${classes.table} (${classes.classid});
