ALTER TABLE ${members.table} ADD KEY `k_@{members.table}_@{members.rolesetid}` (${members.rolesetid});
ALTER TABLE ${members.table} ADD KEY `k_@{members.table}_@{members.table}_@{members.pbwordid}` (${members.pbwordid});
