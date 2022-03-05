-- ALTER TABLE ${members_senses.table} ADD CONSTRAINT `pk_@{members_senses.table}` PRIMARY KEY (${members_senses.classid},${members_senses.vnwordid},${members_senses.sensenum});
ALTER TABLE ${members_senses.table} ADD KEY `k_@{members_senses.table}_@{members_senses.classid}` (${members_senses.classid});
ALTER TABLE ${members_senses.table} ADD KEY `k_@{members_senses.table}_@{members_senses.vnwordid}` (${members_senses.vnwordid});
ALTER TABLE ${members_senses.table} ADD KEY `k_@{members_senses.table}_@{members_senses.synsetid}` (${members_senses.synsetid});
ALTER TABLE ${members_senses.table} ADD KEY `k_@{members_senses.table}_@{members_senses.sensekey}` (${members_senses.sensekey});
