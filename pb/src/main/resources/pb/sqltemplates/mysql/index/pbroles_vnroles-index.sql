-- ALTER TABLE ${pbroles_vnroles.table} ADD CONSTRAINT` `pk_@{pbroles_vnroles.table}`  PRIMARY KEY                                          (${pbroles_vnroles.rolesetid},${pbroles_vnroles.roleid},${pbroles_vnroles.vnclassid},${pbroles_vnroles.vnroleid}));
ALTER TABLE ${pbroles_vnroles.table} ADD KEY            `k_@{pbroles_vnroles.table}_@{pbroles_vnroles.rolesetid}_@{pbroles_vnroles.roleid}` (${pbroles_vnroles.rolesetid},${pbroles_vnroles.roleid});
ALTER TABLE ${pbroles_vnroles.table} ADD KEY            `k_@{pbroles_vnroles.table}_@{pbroles_vnroles.vnclass}_@{pbroles_vnroles.vnrole} `  (${pbroles_vnroles.vnclass},${pbroles_vnroles.vnrole});
