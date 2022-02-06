ALTER TABLE ${args.table} ADD CONSTRAINT fk_${args.table}_${exampleid} FOREIGN KEY (${args.exampleid}) REFERENCES ${examples.table} (${examples.exampleid});
ALTER TABLE ${args.table} ADD CONSTRAINT fk_${args.table}_${narg} FOREIGN KEY (${args.narg}) REFERENCES ${argns.table} (${argns.narg});
ALTER TABLE ${args.table} ADD CONSTRAINT fk_${args.table}_${func} FOREIGN KEY (${args.func}) REFERENCES ${funcs.table} (${funcs.func});
