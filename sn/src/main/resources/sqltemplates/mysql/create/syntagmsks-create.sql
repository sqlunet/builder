CREATE TABLE ${syntagmsks.table} (
    ${syntagmsks.syntagmid} INTEGER NOT NULL,
    ${syntagmsks.sensekey1} VARCHAR (100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    ${syntagmsks.sensekey2} VARCHAR (100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
PRIMARY KEY (${syntagmsks.syntagmid}));
