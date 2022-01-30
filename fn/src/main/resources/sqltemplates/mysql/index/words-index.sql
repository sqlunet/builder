${words}.pk=ALTER TABLE ${words.table} ADD CONSTRAINT ${pk_}${words.table} PRIMARY KEY (${fnwordid});
${words}.no-pk=ALTER TABLE ${words.table} DROP PRIMARY KEY;
${words}.unq1=CREATE UNIQUE INDEX IF NOT EXISTS unq_${words.table}_${word} ON ${words.table} (${word});
${words}.no-unq1=DROP INDEX IF EXISTS unq_${words.table}_${word};
