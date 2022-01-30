ALTER TABLE ${fnwords.table} ADD CONSTRAINT fk_${fnwords.table}_${fnwords.wordid} FOREIGN KEY (${fnwords.wordid}) REFERENCES ${words.table} (${words.wordid});
