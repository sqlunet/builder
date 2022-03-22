ALTER TABLE ${members_groupings.table} ADD CONSTRAINT `uk_@{members_groupings.table}_@{members_groupings.classid}_@{members_groupings.vnwordid}_@{members_groupings.groupingid}` UNIQUE KEY (${members_groupings.classid},${members_groupings.vnwordid},${members_groupings.groupingid});
ALTER TABLE ${members_groupings.table} ADD KEY `k_@{members_groupings.table}_@{members_groupings.classid}` (${members_groupings.classid});
ALTER TABLE ${members_groupings.table} ADD KEY `k_@{members_groupings.table}_@{members_groupings.vnwordid}` (${members_groupings.vnwordid});
ALTER TABLE ${members_groupings.table} ADD KEY `k_@{members_groupings.table}_@{members_groupings.groupingid}` (${members_groupings.groupingid});
