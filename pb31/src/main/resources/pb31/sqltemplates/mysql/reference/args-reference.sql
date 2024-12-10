ALTER TABLE ${args.table} ADD CONSTRAINT `fk_@{args.table}_@{args.exampleid}` FOREIGN KEY (${args.exampleid}) REFERENCES ${examples.table} (${examples.exampleid});
ALTER TABLE ${args.table} ADD CONSTRAINT `fk_@{args.table}_@{args.argtypeid}` FOREIGN KEY (${args.argtypeid}) REFERENCES ${argtypes.table} (${argtypes.argtypeid});
ALTER TABLE ${args.table} ADD CONSTRAINT `fk_@{args.table}_@{args.funcid}` FOREIGN KEY (${args.funcid}) REFERENCES ${funcs.table} (${funcs.funcid});
