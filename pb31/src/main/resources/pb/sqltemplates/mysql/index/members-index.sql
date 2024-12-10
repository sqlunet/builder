ALTER TABLE ${members.table} ADD CONSTRAINT `pk_@{members.table}` PRIMARY KEY (${members.rolesetid},${members.pbwordid});
ALTER TABLE ${members.table} ADD KEY `k_@{members.table}_@{members.rolesetid}` (${members.rolesetid});
ALTER TABLE ${members.table} ADD KEY `k_@{members.table}_@{members.table}_@{members.pbwordid}` (${members.pbwordid});
