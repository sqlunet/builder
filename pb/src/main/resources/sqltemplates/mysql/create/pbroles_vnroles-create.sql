CREATE TABLE IF NOT EXISTS ${pbroles_vnroles.table} (
    ${pbroles_vnroles.rolesetid} INTEGER NOT NULL,
    ${pbroles_vnroles.roleid} INTEGER NOT NULL,
    ${pbroles_vnroles.vnclassid} INTEGER NOT NULL,
    ${pbroles_vnroles.vnroleid} INTEGER NOT NULL,
PRIMARY KEY (${pbroles_vnroles.rolesetid},${pbroles_vnroles.roleid},${pbroles_vnroles.vnclassid},${pbroles_vnroles.vnroleid}));
