CREATE UNIQUE INDEX `pk_@{words.table}` ON ${words.table} (${words.pbwordid});
CREATE UNIQUE INDEX `uk_@{words.table}_@{words.word}` ON ${words.table} (${words.word});
CREATE INDEX `k_@{words.table}_@{words.word}` ON ${words.table} (${words.word});
