CREATE TABLE IF NOT EXISTS ${frames.table} (
    ${frames.frameid} INTEGER NOT NULL,
    ${frames.number} VARCHAR (16) NULL,
    ${frames.xtag} VARCHAR (16) NULL,
    ${frames.framenameid} INTEGER NOT NULL,
    ${frames.framesubnameid} INTEGER NOT NULL,
    ${frames.syntaxid} INTEGER NOT NULL,
    ${frames.semanticsid} INTEGER NOT NULL,
PRIMARY KEY (${frames.frameid}));
