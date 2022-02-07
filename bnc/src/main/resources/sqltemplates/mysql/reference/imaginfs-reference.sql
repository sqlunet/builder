ALTER TABLE ${imaginfs.table} ADD CONSTRAINT fk_${imaginfs.table}_${imaginfs.wordid} FOREIGN KEY (${imaginfs.wordid}) REFERENCES ${wnwords.table} (${wnwords.wordid});
