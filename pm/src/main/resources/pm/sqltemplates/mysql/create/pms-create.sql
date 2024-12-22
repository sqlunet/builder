CREATE TABLE ${pms.table} (
     ${pms.pmid}         INTEGER NOT NULL,
     ${pms.pmroleid}     INTEGER NOT NULL,
--
     ${pms.word}         VARCHAR(24) DEFAULT NULL,
     ${pms.sensekey}     VARCHAR(32) DEFAULT NULL,
--
     ${pms.vn_class}     VARCHAR(12) DEFAULT NULL,
     ${pms.vn_role}      VARCHAR(24) DEFAULT NULL,
--
     ${pms.pb_roleset}   VARCHAR(24) DEFAULT NULL,
     ${pms.pb_role}      VARCHAR(12) DEFAULT NULL,
--
     ${pms.fn_frame}     VARCHAR(36) DEFAULT NULL,
     ${pms.fn_fe}        VARCHAR(32) DEFAULT NULL,
     ${pms.fn_lu}        VARCHAR(24) DEFAULT NULL,
--
     ${pms.source}       INTEGER DEFAULT NULL,
--
     ${pms.wordid}       INTEGER DEFAULT NULL,
     ${pms.synsetid}     INTEGER DEFAULT NULL,
     ${pms.vn_classid}   INTEGER DEFAULT NULL,
     ${pms.vn_roleid}    INTEGER DEFAULT NULL,
     ${pms.vn_wordid}    INTEGER DEFAULT NULL,
     ${pms.pb_rolesetid} INTEGER DEFAULT NULL,
     ${pms.pb_roleid}    INTEGER DEFAULT NULL,
     ${pms.pb_wordid}    INTEGER DEFAULT NULL,
     ${pms.fn_frameid}   INTEGER DEFAULT NULL,
     ${pms.fn_feid}      INTEGER DEFAULT NULL,
     ${pms.fn_fetypeid}  INTEGER DEFAULT NULL,
     ${pms.fn_luid}      INTEGER DEFAULT NULL,
     ${pms.fn_wordid}    INTEGER DEFAULT NULL,
     ${pms.wsource}      INTEGER DEFAULT NULL
);
