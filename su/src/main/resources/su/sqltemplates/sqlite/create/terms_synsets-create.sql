CREATE TABLE ${terms_synsets.table} (
${terms_synsets.mapid} INT NOT NULL,
${terms_synsets.posid} CHARACTER (1) CHECK( ${terms_synsets.posid} IN ('n','v','a','r') ) NOT NULL,
${terms_synsets.maptype} CHARACTER (1) CHECK( ${terms_synsets.maptype} IN ('=','+','@',':','[',']') ) NOT NULL,
${terms_synsets.termid} INT NOT NULL,
${terms_synsets.synsetid} INT NULL DEFAULT NULL
);
