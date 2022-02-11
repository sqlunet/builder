CREATE TABLE IF NOT EXISTS ${members.table} ( ${members.classid} INTEGER NOT NULL, ${members.vnwordid} INTEGER NOT NULL DEFAULT 0,PRIMARY KEY (${members.classid},${members.vnwordid}));
