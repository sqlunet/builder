CREATE TABLE ${terms.table} (
${terms.termid} INT NOT NULL,
${terms.term} VARCHAR(128) mb4 COLLATE utf8mb4_bin NOT NULL,
${terms.wordid} INTEGER NULL DEFAULT NULL
);
