-- ALTER TABLE ${words.table} ADD CONSTRAINT `pk_@{words.table}` PRIMARY KEY (${words.pbwordid});
-- ALTER TABLE ${words.table} ADD CONSTRAINT `uk_@{words.table}_@{words.word}` UNIQUE KEY (${words.word});
ALTER TABLE ${words.table} ADD KEY `k_@{words.table}_@{words.word}` (${words.word});
