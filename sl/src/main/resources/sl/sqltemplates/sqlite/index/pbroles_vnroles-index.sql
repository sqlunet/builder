CREATE INDEX `k_@{pbroles_vnroles.table}_@{pbroles_vnroles.pbroleset}_@{pbroles_vnroles.pbarg}` ON ${pbroles_vnroles.table} (${pbroles_vnroles.pbroleset},${pbroles_vnroles.pbarg});
CREATE INDEX `k_@{pbroles_vnroles.table}_@{pbroles_vnroles.vnclass}_@{pbroles_vnroles.vntheta}` ON ${pbroles_vnroles.table} (${pbroles_vnroles.vnclass},${pbroles_vnroles.vntheta});
