ALTER TABLE ${roles.table} ADD CONSTRAINT `fk_@{roles.table}_@{roles.rolesetid}` FOREIGN KEY (${roles.rolesetid}) REFERENCES ${rolesets.table} (${rolesets.rolesetid});
ALTER TABLE ${roles.table} ADD CONSTRAINT `fk_@{roles.table}_@{roles.funcid}` FOREIGN KEY (${roles.funcid}) REFERENCES ${funcs.table} (${funcs.funcid});
