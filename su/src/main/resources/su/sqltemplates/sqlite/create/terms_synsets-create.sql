CREATE TABLE ${terms_synsets.table} (
${terms_synsets.termid} INT NOT NULL,
${terms_synsets.rel} CHARACTER (1) CHECK( ${terms_synsets.rel} IN ('=','+','@',':','[',']') ) NOT NULL,
${terms_synsets.pos} CHARACTER (1) CHECK( ${terms_synsets.pos} IN ('n','v','a','r') ) NOT NULL,
${terms_synsets.synsetid} INT NOT NULL
);
