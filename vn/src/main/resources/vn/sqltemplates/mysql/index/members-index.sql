ALTER TABLE ${members.table} ADD CONSTRAINT `pk_@{members.table}` PRIMARY KEY (${members.classid},${members.vnwordid});
ALTER TABLE ${members.table} ADD KEY `k_@{members.table}_@{members.classid}` (${members.classid});
ALTER TABLE ${members.table} ADD KEY `k_@{members.table}_@{members.vnwordid}` (${members.vnwordid});
