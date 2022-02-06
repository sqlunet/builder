CREATE TABLE IF NOT EXISTS ${funcs.table} (
    ${funcs.func} INTEGER NOT NULL,
    ${funcs.funcname} VARCHAR (20) NOT NULL,
    ${funcs.funcdescr} VARCHAR (24) NULL,
PRIMARY KEY (${funcs.func}));
