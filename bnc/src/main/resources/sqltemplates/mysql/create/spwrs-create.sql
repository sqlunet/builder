CREATE TABLE ${spwrs.table} (
    ${spwrs.wordid} INTEGER DEFAULT NULL,
    ${convtasks.word} VARCHAR (80) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    ${spwrs.pos} ENUM ('n','v','a','r','s') NOT NULL,
    ${spwrs.freq1} INTEGER DEFAULT NULL,
    ${spwrs.range1} INTEGER DEFAULT NULL,
    ${spwrs.disp1} FLOAT DEFAULT NULL,
    ${spwrs.freq2} INTEGER DEFAULT NULL,
    ${spwrs.range2} INTEGER DEFAULT NULL,
    ${spwrs.disp2} FLOAT DEFAULT NULL,
    ${spwrs.ll} FLOAT DEFAULT NULL,
PRIMARY KEY (${spwrs.word},${spwrs.pos}));
