CREATE TABLE IF NOT EXISTS ${pbrolesets_vnclasses.table} (
    ${pbrolesets_vnclasses.rolesetid} INTEGER     NOT NULL,
    ${pbrolesets_vnclasses.vnclassid} INTEGER     NULL,
    ${pbrolesets_vnclasses.pos}       VARCHAR(1)  NOT NULL,
    ${pbrolesets_vnclasses.pbwordid}  INTEGER     NOT NULL,
    ${pbrolesets_vnclasses.vnclass}   VARCHAR(64) NOT NULL
);
