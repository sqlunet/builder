CREATE TABLE IF NOT EXISTS ${pbrolesets_vnclasses.table} (
    ${pbrolesets_vnclasses.pbrolesetid} INTEGER NULL,
    ${pbrolesets_vnclasses.vnclassid} INTEGER NULL,
    ${pbrolesets_vnclasses.pbroleset} VARCHAR(64) NOT NULL,
    ${pbrolesets_vnclasses.vnclass} VARCHAR(64) NOT NULL
);
