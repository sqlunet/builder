ALTER TABLE ${words.table} ADD CONSTRAINT `fk_@{words.table}_@{words.wordid}` FOREIGN KEY (${words.wordid}) REFERENCES ${wnwords.table} (${wnwords.wordid});
