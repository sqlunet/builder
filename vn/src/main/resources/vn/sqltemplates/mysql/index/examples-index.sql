ALTER TABLE ${examples.table} ADD CONSTRAINT `uniq_@{examples.table}_@{examples.example}` UNIQUE KEY (${examples.example});
