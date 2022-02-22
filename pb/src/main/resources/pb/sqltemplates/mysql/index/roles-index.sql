ALTER TABLE ${restrainedRoles.table} ADD KEY `k_@{restrainedRoles.table}_@{restrainedRoles.roledescr}` (${restrainedRoles.roledescr});
ALTER TABLE ${restrainedRoles.table} ADD KEY `k_@{restrainedRoles.table}_@{restrainedRoles.rolesetid}` (${restrainedRoles.rolesetid});
