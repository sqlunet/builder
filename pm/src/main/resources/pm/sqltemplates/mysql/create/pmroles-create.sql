CREATE TABLE ${pmroles.table} (
    ${pmroles.pmroleid} INTEGER DEFAULT NULL,
    ${pmroles.predicate} VARCHAR (80) NOT NULL,
    ${pmroles.role} VARCHAR (80) NOT NULL,
    ${pmroles.pos} CHARACTER (1) NOT NULL,
PRIMARY KEY (${pmroles.pmroleid}));