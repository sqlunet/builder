ALTER TABLE ${spwrs.table} ADD CONSTRAINT fk_${spwrs.table}_${spwrs.wordid} FOREIGN KEY (${spwrs.wordid}) REFERENCES ${wnwords.table} (${wnwords.wordid});
