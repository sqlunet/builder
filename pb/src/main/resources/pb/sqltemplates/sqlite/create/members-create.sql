CREATE TABLE IF NOT EXISTS ${members.table} (
${members.rolesetid} INTEGER NOT NULL,
${members.pbwordid} INTEGER NOT NULL,
PRIMARY KEY (${members.rolesetid},${members.pbwordid}));
