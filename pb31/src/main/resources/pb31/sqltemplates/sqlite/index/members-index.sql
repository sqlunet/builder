CREATE UNIQUE INDEX `pk_@{members.table}` ON ${members.table} (${members.rolesetid},${members.pbwordid});
CREATE INDEX `k_@{members.table}_@{members.rolesetid}` ON ${members.table} (${members.rolesetid});
CREATE INDEX `k_@{members.table}_@{members.table}_@{members.pbwordid}` ON ${members.table} (${members.pbwordid});
