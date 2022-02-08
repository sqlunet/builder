ALTER TABLE ${pbrolesets_fnframes.table} ADD CONSTRAINT `fk_@{pbrolesets_fnframes.table}_@{pbrolesets_fnframes.rolesetid}` FOREIGN KEY (${pbrolesets_fnframes.rolesetid}) REFERENCES ${rolesets.table} (${rolesets.rolesetid});
ALTER TABLE ${pbrolesets_fnframes.table} ADD CONSTRAINT `fk_@{pbrolesets_fnframes.table}_@{pbrolesets_fnframes.pbwordid}` FOREIGN KEY (${pbrolesets_fnframes.pbwordid}) REFERENCES ${words.table} (${words.pbwordid});
-- ALTER TABLE ${pbrolesets_fnframes.table} ADD CONSTRAINT `fk_@{pbrolesets_fnframes.table}_@{pbrolesets_fnframes.frameid}` FOREIGN KEY (${pbrolesets_fnframes.frameid}) REFERENCES ${fnframes.table} (${fnframes.frameid});