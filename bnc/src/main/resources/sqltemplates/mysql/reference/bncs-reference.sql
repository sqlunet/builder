ALTER TABLE ${bncs.table} ADD CONSTRAINT fk_${bncs.table}_${bncs.wordid} FOREIGN KEY (${bncs.wordid}) REFERENCES ${wnwords.table} (${wnwords.wordid});
