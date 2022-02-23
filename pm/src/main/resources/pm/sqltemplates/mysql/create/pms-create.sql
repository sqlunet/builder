CREATE TABLE ${pms.table} (
     ${pms.pmid}        INTEGER NOT NULL,
     ${pms.pmroleid}    INTEGER NOT NULL,

     ${pms.word}        VARCHAR(33) DEFAULT NULL,
     ${pms.sensekey}    VARCHAR(33) DEFAULT NULL,

     ${pms.vnclass}     VARCHAR(33) DEFAULT NULL,
     ${pms.vnrole}      VARCHAR(33) DEFAULT NULL,
     ${pms.vnword}      VARCHAR(33) DEFAULT NULL,

     ${pms.pbroleset}   VARCHAR(33) DEFAULT NULL,
     ${pms.pbrole}      VARCHAR(33) DEFAULT NULL,

     ${pms.fnframe}     VARCHAR(33) DEFAULT NULL,
     ${pms.fnfe}        VARCHAR(33) DEFAULT NULL,
     ${pms.fnlu}        VARCHAR(33) DEFAULT NULL,
     ${pms.sumo}        VARCHAR(33) DEFAULT NULL,
     ${pms.source}      INTEGER DEFAULT NULL,

     ${pms.wordid}      INTEGER DEFAULT NULL,
     ${pms.synsetid}    INTEGER DEFAULT NULL,
     ${pms.vnclassid}   INTEGER DEFAULT NULL,
     ${pms.vnroleid}    INTEGER DEFAULT NULL,
     ${pms.vnwordid}    INTEGER DEFAULT NULL,
     ${pms.pbrolesetid} INTEGER DEFAULT NULL,
     ${pms.pbroleid}    INTEGER DEFAULT NULL,
     ${pms.pbwordid}    INTEGER DEFAULT NULL,
     ${pms.fnframeid}   INTEGER DEFAULT NULL,
     ${pms.fnfeid}      INTEGER DEFAULT NULL,
     ${pms.fnluid}      INTEGER DEFAULT NULL,
     ${pms.fnwordid}    INTEGER DEFAULT NULL,
     ${pms.wsource}     INTEGER DEFAULT NULL,
PRIMARY KEY (${pms.pmid}));
