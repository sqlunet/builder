ALTER TABLE ${frames_examples.table} ADD CONSTRAINT `pk_@{frames_examples.table}` PRIMARY KEY (${frames_examples.frameid},${frames_examples.exampleid});
ALTER TABLE ${frames_examples.table} ADD KEY `k_@{frames_examples.table}_@{frames_examples.frameid}` (${frames_examples.frameid});
ALTER TABLE ${frames_examples.table} ADD KEY `k_@{frames_examples.table}_@{frames_examples.exampleid}` (${frames_examples.exampleid});
