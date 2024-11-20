ALTER TABLE ${roles.table} ADD CONSTRAINT `fk_@{roles.table}_@{roles.rolesetid}` FOREIGN KEY (${roles.rolesetid}) REFERENCES ${rolesets.table} (${rolesets.rolesetid});
ALTER TABLE ${roles.table} ADD CONSTRAINT `fk_@{roles.table}_@{roles.argtypeid}` FOREIGN KEY (${roles.argtypeid}) REFERENCES ${argtypes.table} (${argtypes.argtypeid});
ALTER TABLE ${roles.table} ADD CONSTRAINT `fk_@{roles.table}_@{roles.vnroleid}` FOREIGN KEY (${roles.vnroleid}) REFERENCES ${vnroles.table} (${vnroles.vnroleid});
ALTER TABLE ${roles.table} ADD CONSTRAINT `fk_@{roles.table}_@{roles.fnfeid}` FOREIGN KEY (${roles.fnfeid}) REFERENCES ${fnfes.table} (${fnfes.fnfeid});
ALTER TABLE ${roles.table} ADD CONSTRAINT `fk_@{roles.table}_@{roles.funcid}` FOREIGN KEY (${roles.funcid}) REFERENCES ${funcs.table} (${funcs.funcid});
