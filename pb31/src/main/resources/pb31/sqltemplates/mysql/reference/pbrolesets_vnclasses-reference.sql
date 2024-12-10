ALTER TABLE ${pbrolesets_vnclasses.table} ADD CONSTRAINT `fk_@{pbrolesets_vnclasses.table}_@{pbrolesets_vnclasses.rolesetid}` FOREIGN KEY (${pbrolesets_vnclasses.rolesetid}) REFERENCES ${rolesets.table} (${rolesets.rolesetid});
ALTER TABLE ${pbrolesets_vnclasses.table} ADD CONSTRAINT `fk_@{pbrolesets_vnclasses.table}_@{pbrolesets_vnclasses.pbwordid}` FOREIGN KEY (${pbrolesets_vnclasses.pbwordid}) REFERENCES ${words.table} (${words.pbwordid});
-- ALTER TABLE ${pbrolesets_vnclasses.table} ADD CONSTRAINT `fk_@{pbrolesets_vnclasses.table}_@{pbrolesets_vnclasses.vnclassid}` FOREIGN KEY (${pbrolesets_vnclasses.vnclassid}) REFERENCES ${vnclasses.table} (${vnclasses.classid});