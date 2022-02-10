-- ALTER TABLE ${words.table} ADD CONSTRAINT `pk_@{words.table}` PRIMARY KEY (${words.fnwordid});
ALTER TABLE ${words.table} ADD CONSTRAINT `uk_@{words.table}_@{words.word}` UNIQUE KEY (${words.word});
