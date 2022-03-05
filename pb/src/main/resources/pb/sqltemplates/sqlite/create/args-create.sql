CREATE TABLE IF NOT EXISTS ${args.table} (
${args.argid} INTEGER NOT NULL,
${args.exampleid} INTEGER NOT NULL,
${args.arg} TEXT,
${args.nargid} VARCHAR (1) NULL,
${args.funcid} INTEGER NULL,
PRIMARY KEY (${args.argid}));
