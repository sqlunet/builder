CREATE TABLE ${predicates.table} (
    ${predicates.predicateid} INTEGER DEFAULT NULL,
    ${predicates.predicate} VARCHAR (80) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
PRIMARY KEY (${predicates.predicateid}));