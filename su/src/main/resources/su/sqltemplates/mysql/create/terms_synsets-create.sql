CREATE TABLE ${terms_synsets.table} (
  ${terms_synsets.termid}  INT NOT NULL,
  ${terms_synsets.rel} ENUM('=','+','@',':','[',']') NOT NULL,
  ${terms_synsets.synsetid} INT NOT NULL
);
