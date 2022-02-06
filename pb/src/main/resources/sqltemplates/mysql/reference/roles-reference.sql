ALTER TABLE ${roles.table} ADD CONSTRAINT fk_${roles.table}_${roles.rolesetid} FOREIGN KEY (${roles.rolesetid}) REFERENCES ${rolesets.table} (${rolesets.rolesetid});
ALTER TABLE ${roles.table} ADD CONSTRAINT fk_${roles.table}_${roles.func} FOREIGN KEY (${roles.func}) REFERENCES ${funcs.table} (${funcs.func});
