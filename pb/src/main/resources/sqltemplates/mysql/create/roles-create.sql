CREATE TABLE IF NOT EXISTS ${roles.table} (
    ${roles.roleid} INTEGER NOT NULL,
    ${roles.rolesetid} INTEGER NOT NULL,
    ${roles.narg} VARCHAR (1 ) NOT NULL,
    ${roles.func} INTEGER NULL,
    ${roles.theta} INTEGER NULL,
    ${roles.roledescr} VARCHAR (100) NOT NULL,
PRIMARY KEY (${roles.roleid}));
