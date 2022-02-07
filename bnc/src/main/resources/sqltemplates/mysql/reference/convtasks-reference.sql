ALTER TABLE ${convtasks.table} ADD CONSTRAINT fk_${convtasks.table}_${convtasks.wordid} FOREIGN KEY (${convtasks.wordid}) REFERENCES ${wnwords.table} (${wnwords.wordid});
