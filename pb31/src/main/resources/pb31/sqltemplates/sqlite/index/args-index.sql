CREATE UNIQUE INDEX `pk_@{args.table}` ON ${args.table} (${args.argid});
CREATE INDEX `k_@{args.table}_@{args.arg}` ON ${args.table} (${args.arg});
