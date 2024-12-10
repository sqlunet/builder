ALTER TABLE ${examples.table} ADD CONSTRAINT `pk_@{examples.table}` PRIMARY KEY (${examples.exampleid});
ALTER TABLE ${examples.table} ADD KEY `k_@{examples.table}_@{examples.examplename}` (${examples.examplename});
ALTER TABLE ${examples.table} ADD KEY `k_@{examples.table}_@{examples.rolesetid}` (${examples.rolesetid});
