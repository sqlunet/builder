ALTER TABLE ${words.table} ADD CONSTRAINT `uk_@{words.table}_@{words.word}` UNIQUE KEY (${words.word});
