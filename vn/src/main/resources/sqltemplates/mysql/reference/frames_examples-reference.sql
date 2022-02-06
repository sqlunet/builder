ALTER TABLE ${frames_examples.table} ADD CONSTRAINT fk_${frames_examples.table}_${frames_examples.frameid} FOREIGN KEY (${frames_examples.frameid}) REFERENCES ${frames.table} (${frames.frameid});
ALTER TABLE ${frames_examples.table} ADD CONSTRAINT fk_${frames_examples.table}_${frames_examples.exampleid} FOREIGN KEY (${frames_examples.exampleid}) REFERENCES ${examples.table} (${examples.exampleid});
