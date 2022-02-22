CREATE TABLE IF NOT EXISTS ${roles.table} (
    ${roles.roleid} INTEGER NOT NULL,
    ${roles.classid} INTEGER NOT NULL,
    ${roles.roletypeid} INTEGER NOT NULL,
    ${roles.restrsid} INTEGER NULL,
 PRIMARY KEY (${roles.roleid}));
