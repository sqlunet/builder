CREATE UNIQUE INDEX `pk_@{examples.table}` ON ${examples.table} (${examples.exampleid});
CREATE UNIQUE INDEX `uk_@{examples.table}_@{examples.example}` ON ${examples.table} (${examples.example});
