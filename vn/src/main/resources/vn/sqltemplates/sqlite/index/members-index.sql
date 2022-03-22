CREATE UNIQUE INDEX `pk_@{members.table}` ON ${members.table} (${members.classid},${members.vnwordid});
CREATE INDEX `k_@{members.table}_@{members.classid}` ON ${members.table} (${members.classid});
CREATE INDEX `k_@{members.table}_@{members.vnwordid}` ON ${members.table} (${members.vnwordid});
