ALTER TABLE ${words.table} ADD CONSTRAINT `uniq_@{words.table}_@{words.word}` UNIQUE KEY (${words.word});
