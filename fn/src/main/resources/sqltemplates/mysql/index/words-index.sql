-- ALTER TABLE ${fnwords.table} ADD CONSTRAINT pk_${fnwords.table} PRIMARY KEY (${fnwords.fnwordid});
CREATE UNIQUE INDEX IF NOT EXISTS unq_${fnwords.table}_${fnwords.word} ON ${fnwords.table} (${fnwords.word});
