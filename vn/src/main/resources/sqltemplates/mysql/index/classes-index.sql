ALTER TABLE ${classes.table} ADD CONSTRAINT `uniq_@{classes.table}_@{classes.class}` UNIQUE KEY (${classes.class});
ALTER TABLE ${classes.table} ADD CONSTRAINT `uniq_@{classes.table}_@{classes.classtag}` UNIQUE KEY (${classes.classtag});
