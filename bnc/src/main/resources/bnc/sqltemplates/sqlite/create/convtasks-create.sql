CREATE TABLE ${convtasks.table} (
${convtasks.wordid} INTEGER DEFAULT NULL,
${convtasks.word} VARCHAR (80) NOT NULL,
${convtasks.posid} CHARACTER (1) CHECK( ${convtasks.posid} IN ('n','v','a','r','s') ) NOT NULL,
${convtasks.freq1} INTEGER DEFAULT NULL,
${convtasks.range1} INTEGER DEFAULT NULL,
${convtasks.disp1} FLOAT DEFAULT NULL,
${convtasks.freq2} INTEGER DEFAULT NULL,
${convtasks.range2} INTEGER DEFAULT NULL,
${convtasks.disp2} FLOAT DEFAULT NULL,
${convtasks.ll} FLOAT DEFAULT NULL
);
