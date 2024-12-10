ALTER TABLE ${args.table} ADD CONSTRAINT `pk_@{args.table}` PRIMARY KEY (${args.argid});
ALTER TABLE ${args.table} ADD KEY `k_@{args.table}_@{args.arg}` (${args.arg}(100));
