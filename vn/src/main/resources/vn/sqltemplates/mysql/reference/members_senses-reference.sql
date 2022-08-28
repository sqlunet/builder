ALTER TABLE ${members_senses.table} ADD CONSTRAINT `fk_@{members_senses.table}_@{members_senses.classid}` FOREIGN KEY (${members_senses.classid}) REFERENCES ${classes.table} (${classes.classid});
ALTER TABLE ${members_senses.table} ADD CONSTRAINT `fk_@{members_senses.table}_@{members_senses.vnwordid}` FOREIGN KEY (${members_senses.vnwordid}) REFERENCES ${words.table} (${words.vnwordid});
-- ALTER TABLE ${members_senses.table} ADD CONSTRAINT `fk_@{members_senses.table}_@{members_senses.synsetid}` FOREIGN KEY (${members_senses.synsetid}) REFERENCES ${wnsynsets.table} (${wnsynsets.synsetid});
-- ALTER TABLE ${members_senses.table} ADD CONSTRAINT `fk_@{members_senses.table}_@{members_senses.wordid}` FOREIGN KEY (${members_senses.wordid}) REFERENCES ${wnwords.table} (${wnwords.wordid});
