ALTER TABLE ${args.table} ADD CONSTRAINT `fk_@{args.table}_@{args.exampleid}` FOREIGN KEY (${args.exampleid}) REFERENCES ${examples.table} (${examples.exampleid});
ALTER TABLE ${args.table} ADD CONSTRAINT `fk_@{args.table}_@{args.nargid}` FOREIGN KEY (${args.nargid}) REFERENCES ${argns.table} (${argns.nargid});
ALTER TABLE ${args.table} ADD CONSTRAINT `fk_@{args.table}_@{args.funcid}` FOREIGN KEY (${args.funcid}) REFERENCES ${funcs.table} (${funcs.funcid});
