CREATE TABLE IF NOT EXISTS ${args.table} (
    ${args.argid} INTEGER NOT NULL,
    ${args.exampleid} INTEGER NOT NULL,
    ${args.arg} TEXT,
    ${args.argtypeid} VARCHAR (1) NULL,
    ${args.funcid} INTEGER NULL
);
