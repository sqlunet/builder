CREATE TABLE ${bncs.table} (
${bncs.wordid} INTEGER DEFAULT NULL,
${bncs.word} VARCHAR (80) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
${bncs.pos} ENUM ('n','v','a','r','s') NOT NULL,
${bncs.freq} INTEGER DEFAULT NULL,
${bncs.range} INTEGER DEFAULT NULL,
${bncs.disp} FLOAT DEFAULT NULL
)
DEFAULT CHARSET=utf8mb3;
