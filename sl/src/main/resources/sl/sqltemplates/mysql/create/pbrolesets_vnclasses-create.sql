CREATE TABLE IF NOT EXISTS ${pbrolesets_vnclasses.table} (
    ${pbrolesets_vnclasses.pbroleset} VARCHAR(64) NOT NULL,
    ${pbrolesets_vnclasses.vnclass} VARCHAR(64) NOT NULL,
    ${pbrolesets_vnclasses.pbrolesetid} INTEGER NULL,
    ${pbrolesets_vnclasses.vnclassid} INTEGER NULL,
PRIMARY KEY (${pbrolesets_vnclasses.pbroleset},${pbrolesets_vnclasses.vnclass}));
