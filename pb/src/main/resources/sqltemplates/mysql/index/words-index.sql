CREATE UNIQUE INDEX IF NOT EXISTS unq_${words.table}_${words.word} ON ${words.table} (${words.word});
