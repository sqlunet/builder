CREATE TABLE IF NOT EXISTS ${restrainedRoles.table} (
    ${restrainedRoles.roleid} INTEGER NOT NULL,
    ${restrainedRoles.rolesetid} INTEGER NOT NULL,
    ${restrainedRoles.nargid} VARCHAR (1 ) NOT NULL,
    ${restrainedRoles.funcid} INTEGER NULL,
    ${restrainedRoles.roledescr} VARCHAR (100) NULL,
PRIMARY KEY (${restrainedRoles.roleid}));
