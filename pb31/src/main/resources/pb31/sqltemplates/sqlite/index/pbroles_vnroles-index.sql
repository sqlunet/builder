CREATE INDEX `k_@{pbroles_vnroles.table}_@{pbroles_vnroles.rolesetid}_@{pbroles_vnroles.roleid}` ON ${pbroles_vnroles.table} (${pbroles_vnroles.rolesetid},${pbroles_vnroles.roleid});
CREATE INDEX `k_@{pbroles_vnroles.table}_@{pbroles_vnroles.vnclass}_@{pbroles_vnroles.vntheta}` ON ${pbroles_vnroles.table} (${pbroles_vnroles.vnclass},${pbroles_vnroles.vntheta});
