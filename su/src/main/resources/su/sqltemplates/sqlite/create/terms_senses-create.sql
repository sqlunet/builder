CREATE TABLE ${terms_senses.table} (
${terms_senses.termid} INT NOT NULL,
${terms_senses.rel} CHARACTER (1) CHECK( ${terms_senses.rel} IN ('=','+','@',':','[',']') ) NOT NULL,
${terms_senses.sensekey} VARCHAR (100) NULL
${terms_senses.synsetid} INT NULL
${terms_senses.wordid} INT NULL
);
