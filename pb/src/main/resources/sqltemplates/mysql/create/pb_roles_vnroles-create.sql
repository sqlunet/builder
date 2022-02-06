CREATE TABLE IF NOT EXISTS ${pbroles_vnroles.table} (
    ${pbroles_vnrolesrolesetid} INTEGER NOT NULL,
    ${pbroles_vnrolesroleid} INTEGER NOT NULL,
    ${pbroles_vnrolesvnclassid} INTEGER NOT NULL,
    ${pbroles_vnrolesnroleid} INTEGER NOT NULL,
PRIMARY KEY (${pbroles_vnroles.rolesetid},${pbroles_vnroles.roleid},${pbroles_vnroles.vnclassid},${pbroles_vnroles.vnroleid}));
