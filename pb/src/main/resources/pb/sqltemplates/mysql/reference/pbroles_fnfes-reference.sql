ALTER TABLE ${pbroles_fnfes.table} ADD CONSTRAINT `fk_@{pbroles_fnfes.table}_@{pbroles_fnfes.rolesetid}` FOREIGN KEY (${pbroles_fnfes.rolesetid}) REFERENCES ${rolesets.table} (${rolesets.rolesetid});
ALTER TABLE ${pbroles_fnfes.table} ADD CONSTRAINT `fk_@{pbroles_fnfes.table}_@{pbroles_fnfes.roleid}` FOREIGN KEY (${pbroles_fnfes.roleid}) REFERENCES ${roles.table} (${roles.roleid});
-- ALTER TABLE ${pbroles_fnfes.table} ADD CONSTRAINT `fk_@{pbroles_fnfes.table}_@{pbroles_fnfes.fnframeid}` FOREIGN KEY (${pbroles_fnfes.fnframeid}) REFERENCES ${fn_frames.table} (${fn_frames.frameid});
-- ALTER TABLE ${pbroles_fnfes.table} ADD CONSTRAINT `fk_@{pbroles_fnfes.table}_@{pbroles_fnfes.fnfeid}` FOREIGN KEY (${pbroles_fnfes.fnfeid}) REFERENCES ${fn_fes.table} (${fn_fes.feid});
