CREATE TABLE ${syntagms.table} (
    ${syntagms.syntagmid} INTEGER NOT NULL,
    ${syntagms.synset1id} INTEGER NULL,
    ${syntagms.word1id} INTEGER NULL,
    ${syntagms.synset2id} INTEGER NULL,
    ${syntagms.word2id} INTEGER NULL,
    ${syntagmsks.sensekey1} VARCHAR (100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    ${syntagmsks.sensekey2} VARCHAR (100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
PRIMARY KEY (${syntagms.syntagmid}));
