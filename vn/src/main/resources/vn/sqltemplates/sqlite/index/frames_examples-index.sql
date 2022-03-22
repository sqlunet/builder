CREATE UNIQUE INDEX `pk_@{frames_examples.table}` ON ${frames_examples.table} (${frames_examples.frameid},${frames_examples.exampleid});
CREATE INDEX `k_@{frames_examples.table}_@{frames_examples.frameid}` ON ${frames_examples.table} (${frames_examples.frameid});
CREATE INDEX `k_@{frames_examples.table}_@{frames_examples.exampleid}` ON ${frames_examples.table} (${frames_examples.exampleid});
