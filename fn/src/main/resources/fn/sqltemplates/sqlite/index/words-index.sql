CREATE UNIQUE INDEX `pk_@{words.table}` ON ${words.table} (${words.fnwordid});
CREATE UNIQUE INDEX `uk_@{words.table}_@{words.word}` ON ${words.table} (${words.word});
CREATE INDEX `k_@{words.table}_@{words.word}` ON ${words.table} (${words.word});
