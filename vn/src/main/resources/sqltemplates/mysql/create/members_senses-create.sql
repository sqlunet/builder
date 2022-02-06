CREATE TABLE IF NOT EXISTS ${members_senses.table} (
    ${members_senses.classid} INTEGER NOT NULL,
    ${members_senses.vnwordid} INTEGER NOT NULL DEFAULT 0,
    ${members_senses.sensenum} INTEGER NOT NULL DEFAULT 0,
    ${members_senses.synsetid} INTEGER DEFAULT NULL,
    ${members_senses.sensekey} VARCHAR (100) DEFAULT NULL,
    ${members_senses.quality} FLOAT NOT NULL,
PRIMARY KEY (${members_senses.classid},${members_senses.vnwordid},${members_senses.sensenum}));
