CREATE TABLE ${imaginfs.table} (
    ${imaginfs.wordid} INTEGER NOT NULL DEFAULT '0',
    ${imaginfs.pos} ENUM ('n','v','a','r','s') NOT NULL,
    ${imaginfs.freq1} INTEGER DEFAULT NULL,
    ${imaginfs.range1} INTEGER DEFAULT NULL,
    ${imaginfs.disp1} FLOAT DEFAULT NULL,
    ${imaginfs.freq2} INTEGER DEFAULT NULL,
    ${imaginfs.range2} INTEGER DEFAULT NULL,
    ${imaginfs.disp2} FLOAT DEFAULT NULL,
    ${imaginfs.ll} FLOAT DEFAULT NULL,
PRIMARY KEY (${imaginfs.wordid},${imaginfs.pos}));
