CREATE TABLE IF NOT EXISTS ${pbroles_vnroles.table} (
    ${pbroles_vnroles.rolesetid} INTEGER NOT NULL,
    ${pbroles_vnroles.roleid} INTEGER NOT NULL,
    ${pbroles_vnroles.vnclassid} INTEGER NULL,
    ${pbroles_vnroles.vnroleid} INTEGER NULL,
    ${pbroles_vnroles.vnroletypeid} INTEGER NULL,
    ${pbroles_vnroles.vnclass} VARCHAR(64) NOT NULL,
    ${pbroles_vnroles.vntheta} VARCHAR(20) NOT NULL,
PRIMARY KEY (${pbroles_vnroles.rolesetid},${pbroles_vnroles.roleid},${pbroles_vnroles.vnclass},${pbroles_vnroles.vntheta}));

-- PRIMARY KEY (${pbroles_vnroles.rolesetid},${pbroles_vnroles.roleid},${pbroles_vnroles.vnclassid},${pbroles_vnroles.vnroleid}));
