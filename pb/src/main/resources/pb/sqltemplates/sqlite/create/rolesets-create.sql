CREATE TABLE IF NOT EXISTS ${rolesets.table} (
${rolesets.rolesetid} INTEGER NOT NULL,
${rolesets.rolesethead} VARCHAR (24) NOT NULL,
${rolesets.rolesetname} VARCHAR (64) NOT NULL,
${rolesets.rolesetdescr} TEXT NULL,
${rolesets.pbwordid} INTEGER NULL,
PRIMARY KEY (${rolesets.rolesetid}));
