ALTER TABLE ${examples.table} ADD CONSTRAINT `pk_@{examples.table}` PRIMARY KEY (${examples.exampleid});
ALTER TABLE ${examples.table} ADD CONSTRAINT `uk_@{examples.table}_@{examples.example}` UNIQUE KEY (${examples.example}(255));
