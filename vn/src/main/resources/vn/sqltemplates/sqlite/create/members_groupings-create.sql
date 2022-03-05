CREATE TABLE IF NOT EXISTS ${members_groupings.table} (
${members_groupings.classid} INTEGER NOT NULL,
${members_groupings.vnwordid} INTEGER NOT NULL,
${members_groupings.groupingid} INTEGER NOT NULL,
UNIQUE KEY (${members_groupings.classid},${members_groupings.vnwordid},${members_groupings.groupingid}));
