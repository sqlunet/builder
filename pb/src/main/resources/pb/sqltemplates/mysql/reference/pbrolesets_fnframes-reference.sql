ALTER TABLE ${pbrolesets_fnframes.table} ADD CONSTRAINT `fk_@{pbrolesets_fnframes.table}_@{pbrolesets_fnframes.rolesetid}` FOREIGN KEY (${pbrolesets_fnframes.rolesetid}) REFERENCES ${rolesets.table} (${rolesets.rolesetid});
ALTER TABLE ${pbrolesets_fnframes.table} ADD CONSTRAINT `fk_@{pbrolesets_fnframes.table}_@{pbrolesets_fnframes.pbwordid}` FOREIGN KEY (${pbrolesets_fnframes.pbwordid}) REFERENCES ${words.table} (${words.pbwordid});
-- ALTER TABLE ${pbrolesets_fnframes.table} ADD CONSTRAINT `fk_@{pbrolesets_fnframes.table}_@{pbrolesets_fnframes.fnframeid}` FOREIGN KEY (${pbrolesets_fnframes.fnframeid}) REFERENCES ${fnframes.table} (${fnframes.fnframeid});
