CREATE TABLE ${bncs.table} (
${bncs.wordid} INTEGER DEFAULT NULL,
${bncs.word} VARCHAR (80) NOT NULL,
${bncs.posid} CHARACTER (1) CHECK( ${bncs.posid} IN ('n','v','a','r','s') ) NOT NULL,
${bncs.freq} INTEGER DEFAULT NULL,
${bncs.range} INTEGER DEFAULT NULL,
${bncs.disp} FLOAT DEFAULT NULL
);
