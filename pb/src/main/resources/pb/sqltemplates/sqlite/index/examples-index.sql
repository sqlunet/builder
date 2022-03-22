CREATE UNIQUE INDEX `pk_@{examples.table}` ON ${examples.table} (${examples.exampleid});
CREATE INDEX `k_@{examples.table}_@{examples.examplename}` ON ${examples.table} (${examples.examplename});
CREATE INDEX `k_@{examples.table}_@{examples.rolesetid}` ON ${examples.table} (${examples.rolesetid});
