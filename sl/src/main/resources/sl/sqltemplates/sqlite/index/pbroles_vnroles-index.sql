ALTER TABLE,${pbroles_vnroles.table} ADD KEY `k_${pbroles_vnroles.table}_@{pbroles_vnroles.pbroleset}_@{pbroles_vnroles.pbarg}` (${pbroles_vnroles.pbroleset},${pbroles_vnroles.pbarg});ALTER TABLE,${pbroles_vnroles.table} ADD KEY `k_${pbroles_vnroles.table}_@{pbroles_vnroles.vnclass}_@{pbroles_vnroles.vntheta}` (${pbroles_vnroles.vnclass},${pbroles_vnroles.vntheta});
