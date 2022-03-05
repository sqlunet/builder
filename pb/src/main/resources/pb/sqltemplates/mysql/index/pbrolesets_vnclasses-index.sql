-- ALTER TABLE ${pbrolesets_vnclasses.table} ADD CONSTRAINT `pk_@{pbrolesets_vnclasses.table}` PRIMARY KEY                      (${pbrolesets_vnclasses.rolesetid},${pbrolesets_vnclasses.vnclassid},${pbrolesets_vnclasses.pos},${pbrolesets_vnclasses.pbwordid});
ALTER TABLE ${pbrolesets_vnclasses.table} ADD KEY        `k_@{pbrolesets_vnclasses.table}_@{pbrolesets_vnclasses.rolesetid}` (${pbrolesets_vnclasses.rolesetid});
ALTER TABLE ${pbrolesets_vnclasses.table} ADD KEY        `k_@{pbrolesets_vnclasses.table}_@{pbrolesets_vnclasses.vnclass}`   (${pbrolesets_vnclasses.rolesetid});
