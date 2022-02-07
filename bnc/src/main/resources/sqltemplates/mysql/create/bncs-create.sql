CREATE TABLE ${bncs.table} (
    ${bncs.wordid} INTEGER NOT NULL DEFAULT '0',
    ${bncs.pos} ENUM ('n','v','a','r','s') NOT NULL,
    ${bncs.freq} INTEGER DEFAULT NULL,
    ${bncs.range} INTEGER DEFAULT NULL,
    ${bncs.disp} FLOAT DEFAULT NULL,
PRIMARY KEY (${bncs.wordid},${bncs.pos));
