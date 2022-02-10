ALTER TABLE ${members_groupings.table} ADD KEY `k_@{members_groupings.table}_@{members_groupings.classid}` (${members_groupings.classid});
ALTER TABLE ${members_groupings.table} ADD KEY `k_@{members_groupings.table}_@{members_groupings.vnwordid}` (${members_groupings.vnwordid});
ALTER TABLE ${members_groupings.table} ADD KEY `k_@{members_groupings.table}_@{members_groupings.groupingid}` (${members_groupings.groupingid});
