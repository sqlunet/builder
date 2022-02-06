ALTER TABLE ${rels.table} ADD CONSTRAINT fk_${rels.table}_${rels.exampleid} FOREIGN KEY (${rels.exampleid}) REFERENCES ${examples.table} (${examples.exampleid});
