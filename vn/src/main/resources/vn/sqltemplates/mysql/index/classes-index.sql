ALTER TABLE ${classes.table} ADD CONSTRAINT `pk_@{classes.table}` PRIMARY KEY (${classes.classid});
ALTER TABLE ${classes.table} ADD CONSTRAINT `uk_@{classes.table}_@{classes.class}` UNIQUE KEY (${classes.class});
ALTER TABLE ${classes.table} ADD CONSTRAINT `uk_@{classes.table}_@{classes.classtag}` UNIQUE KEY (${classes.classtag});
