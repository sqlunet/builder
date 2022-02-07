CREATE TABLE ${convtasks.table} (
    ${convtasks.wordid} INTEGER NOT NULL DEFAULT '0',
    ${convtasks.pos} ENUM ('n','v','a','r','s') NOT NULL,
    ${convtasks.freq1} INTEGER DEFAULT NULL,
    ${convtasks.range1} INTEGER DEFAULT NULL,
    ${convtasks.disp1} FLOAT DEFAULT NULL,
    ${convtasks.freq2} INTEGER DEFAULT NULL,
    ${convtasks.range2} INTEGER DEFAULT NULL,
    ${convtasks.disp2} FLOAT DEFAULT NULL,
    ${convtasks.ll} FLOAT DEFAULT NULL,
PRIMARY KEY (${convtasks.wordid},${convtasks.pos}));
