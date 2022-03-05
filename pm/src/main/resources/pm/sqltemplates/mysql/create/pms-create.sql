CREATE TABLE ${pms.table} (
     ${pms.pmid}        INTEGER NOT NULL,
     ${pms.pmroleid}    INTEGER NOT NULL,
--
     ${pms.word}        VARCHAR(24) DEFAULT NULL,
     ${pms.sensekey}    VARCHAR(32) DEFAULT NULL,
--
     ${pms.vnclass}     VARCHAR(12) DEFAULT NULL,
     ${pms.vnrole}      VARCHAR(24) DEFAULT NULL,
--
     ${pms.pbroleset}   VARCHAR(24) DEFAULT NULL,
     ${pms.pbrole}      VARCHAR(12) DEFAULT NULL,
--
     ${pms.fnframe}     VARCHAR(36) DEFAULT NULL,
     ${pms.fnfe}        VARCHAR(32) DEFAULT NULL,
     ${pms.fnlu}        VARCHAR(24) DEFAULT NULL,
     ${pms.sumo}        VARCHAR(64) DEFAULT NULL,
     ${pms.source}      INTEGER DEFAULT NULL,
--
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
