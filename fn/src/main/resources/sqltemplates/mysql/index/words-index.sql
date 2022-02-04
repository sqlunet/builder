-- ALTER TABLE ${words.table} ADD CONSTRAINT `pk_@{words.table}` PRIMARY KEY (${words.fnwordid});
CREATE UNIQUE INDEX `unq_@{words.table}_@{words.word}` ON ${words.table} (${words.word});
