-- ALTER TABLE ${pbrolesets_vnclasses.table} ADD CONSTRAINT `fk_@{pbrolesets_vnclasses.table}_@{pbrolesets_vnclasses.pbrolesetid}` FOREIGN KEY (${pbrolesets_vnclasses.pbrolesetid}) REFERENCES ${pbrolesets.table} (${pbrolesets.rolesetid});
-- ALTER TABLE ${pbrolesets_vnclasses.table} ADD CONSTRAINT `fk_@{pbrolesets_vnclasses.table}_@{pbrolesets_vnclasses.vnclassid}` FOREIGN KEY (${pbrolesets_vnclasses.vnclassid}) REFERENCES ${vnclasses.table} (${vnclasses.classid});
