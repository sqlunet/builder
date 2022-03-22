CREATE UNIQUE INDEX `uk_@{members_groupings.table}_@{members_groupings.classid}_@{members_groupings.vnwordid}_@{members_groupings.groupingid}` ON ${members_groupings.table} (${members_groupings.classid},${members_groupings.vnwordid},${members_groupings.groupingid});
CREATE INDEX `k_@{members_groupings.table}_@{members_groupings.classid}` ON ${members_groupings.table} (${members_groupings.classid});
CREATE INDEX `k_@{members_groupings.table}_@{members_groupings.vnwordid}` ON ${members_groupings.table} (${members_groupings.vnwordid});
CREATE INDEX `k_@{members_groupings.table}_@{members_groupings.groupingid}` ON ${members_groupings.table} (${members_groupings.groupingid});
