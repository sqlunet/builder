ALTER TABLE ${rolesets.table} ADD KEY `k_@{rolesets.table}_@{rolesets.rolesethead}` (${rolesets.rolesethead});
ALTER TABLE ${rolesets.table} ADD KEY `k_@{rolesets.table}_@{rolesets.rolesetname}` (${rolesets.rolesetname});
ALTER TABLE ${rolesets.table} ADD KEY `k_@{rolesets.table}_@{rolesets.pbwordid}` (${rolesets.pbwordid});
