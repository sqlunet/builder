CREATE TABLE IF NOT EXISTS ${roles.table} (
${roles.roleid} INTEGER NOT NULL,
${roles.rolesetid} INTEGER NOT NULL,
${roles.argtypeid} VARCHAR (1 ) NOT NULL,
${roles.funcid} INTEGER NULL,
${roles.vnroleid} INTEGER NULL,
${roles.fnfeid} INTEGER NULL,
${roles.roledescr} VARCHAR (100) NULL
);
