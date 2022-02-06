CREATE TABLE IF NOT EXISTS ${args.table} (
    ${args.argid} INTEGER NOT NULL,
    ${args.exampleid} INTEGER NOT NULL,
    ${args.arg} TEXT,
    ${args.narg} VARCHAR (1) NULL,
    ${args.func} INTEGER NULL,
PRIMARY KEY (${args.argid}));
