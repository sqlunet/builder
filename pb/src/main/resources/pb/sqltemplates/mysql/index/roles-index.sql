ALTER TABLE ${roles.table} ADD KEY `k_@{roles.table}_@{roles.roledescr}` (${roles.roledescr});
ALTER TABLE ${roles.table} ADD KEY `k_@{roles.table}_@{roles.rolesetid}` (${roles.rolesetid});
