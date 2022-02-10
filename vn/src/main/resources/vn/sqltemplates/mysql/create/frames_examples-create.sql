CREATE TABLE IF NOT EXISTS ${frames_examples.table} (
    ${frames_examples.frameid} INTEGER NOT NULL,
    ${frames_examples.exampleid} INTEGER NOT NULL,
PRIMARY KEY (${frames_examples.frameid},${frames_examples.exampleid}));
