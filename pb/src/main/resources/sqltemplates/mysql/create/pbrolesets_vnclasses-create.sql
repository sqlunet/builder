CREATE TABLE IF NOT EXISTS ${pbrolesets_vnclasses.table} (
    ${pbrolesets_vnclasses.rolesetid} INTEGER     NOT NULL,
    ${pbrolesets_vnclasses.pos}       VARCHAR(1)  NOT NULL,
    ${pbrolesets_vnclasses.pbwordid}  INTEGER     NOT NULL,
    ${pbrolesets_vnclasses.vnclass}   VARCHAR(64) NOT NULL,
    ${pbrolesets_vnclasses.vnclassid} INTEGER     NULL,
PRIMARY KEY (${pbrolesets_vnclasses.rolesetid},${pbrolesets_vnclasses.vnclass},${pbrolesets_vnclasses.pos},${pbrolesets_vnclasses.pbwordid}));

-- PRIMARY KEY (${pbrolesets_vnclasses.rolesetid},${pbrolesets_vnclasses.vnclassid},${pbrolesets_vnclasses.pos},${pbrolesets_vnclasses.pbwordid}));
