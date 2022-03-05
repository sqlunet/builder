CREATE TABLE IF NOT EXISTS ${frames.table} (
${frames.frameid} INTEGER NOT NULL,
${frames.frame} VARCHAR(40),
${frames.framedefinition} TEXT,
${frames.cdate} VARCHAR(27),
${frames.cby} VARCHAR(5),
PRIMARY KEY (${frames.frameid}) );
