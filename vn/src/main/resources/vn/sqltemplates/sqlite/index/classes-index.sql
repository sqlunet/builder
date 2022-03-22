CREATE UNIQUE INDEX `pk_@{classes.table}` ON ${classes.table} (${classes.classid});
CREATE UNIQUE INDEX `uk_@{classes.table}_@{classes.class}` ON ${classes.table} (${classes.class});
CREATE UNIQUE INDEX `uk_@{classes.table}_@{classes.classtag}` ON ${classes.table} (${classes.classtag});
