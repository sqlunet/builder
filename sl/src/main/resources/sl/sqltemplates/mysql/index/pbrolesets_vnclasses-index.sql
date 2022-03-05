-- ALTER TABLE ${pbrolesets_vnclasses.table} ADD CONSTRAINT `pk_@{pbrolesets_vnclasses.table}` PRIMARY KEY                      (${pbrolesets_vnclasses.pbrolesetid},${pbrolesets_vnclasses.vnclassid});
ALTER TABLE ${pbrolesets_vnclasses.table} ADD KEY        `k_@{pbrolesets_vnclasses.table}_${pbrolesets_vnclasses.pbroleset}` (${pbrolesets_vnclasses.pbroleset});
ALTER TABLE ${pbrolesets_vnclasses.table} ADD KEY        `k_@{pbrolesets_vnclasses.table}_${pbrolesets_vnclasses.vnclass}`   (${pbrolesets_vnclasses.vnclass});
