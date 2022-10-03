CREATE TABLE ${files.table} (
${files.fileid} INT NOT NULL,
${files.file} VARCHAR(128) mb4 NOT NULL,
${files.version} VARCHAR(5) mb4 DEFAULT NULL,
${files.date} DATETIME DEFAULT NULL
);
