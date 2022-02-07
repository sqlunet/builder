CREATE TABLE ${spwrs.table} (
    ${spwrs.wordid} INTEGER NOT NULL DEFAULT '0',
    ${spwrs.pos} ENUM ('n','v','a','r','s') NOT NULL,
    ${spwrs.freq1} INTEGER DEFAULT NULL,
    ${spwrs.range1} INTEGER DEFAULT NULL,
    ${spwrs.disp1} FLOAT DEFAULT NULL,
    ${spwrs.freq2} INTEGER DEFAULT NULL,
    ${spwrs.range2} INTEGER DEFAULT NULL,
    ${spwrs.disp2} FLOAT DEFAULT NULL,
    ${spwrs.ll} FLOAT DEFAULT NULL,
PRIMARY KEY (${spwrs.wordid},${spwrs.pos}));
