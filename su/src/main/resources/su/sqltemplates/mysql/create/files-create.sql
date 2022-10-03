CREATE TABLE ${files.table} (
  ${files.fileid} INT NOT NULL,
  ${files.file} VARCHAR(128) CHARACTER SET utf8mb4 NOT NULL,
  ${files.version} VARCHAR(5) CHARACTER SET utf8mb4 DEFAULT NULL,
  ${files.date} DATETIME DEFAULT NULL
);
