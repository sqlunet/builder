CREATE TABLE IF NOT EXISTS ${pbroles_vnroles.table} (
    ${pbroles_vnroles.pbrolesetid} INTEGER NULL,
    ${pbroles_vnroles.pbroleid} INTEGER NULL,
    ${pbroles_vnroles.vnclassid} INTEGER NULL,
    ${pbroles_vnroles.vnroleid} INTEGER NULL,
    ${pbroles_vnroles.vnroletypeid} INTEGER NULL,
    ${pbroles_vnroles.pbroleset} VARCHAR(64) NOT NULL,
    ${pbroles_vnroles.pbarg} VARCHAR(1) NOT NULL,
    ${pbroles_vnroles.vnclass} VARCHAR(64) NOT NULL,
    ${pbroles_vnroles.vntheta} VARCHAR(20) NOT NULL
);
