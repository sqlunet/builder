ALTER TABLE ${members.table} ADD CONSTRAINT fk_${members.table}_${members.classid} FOREIGN KEY (${members.classid}) REFERENCES ${classes.table} (${classes.classid});
ALTER TABLE ${members.table} ADD CONSTRAINT fk_${members.table}_${members.vnwordid} FOREIGN KEY (${members.vnwordid}) REFERENCES ${words.table} (${words.wordid});
