CREATE UNIQUE INDEX `pk_@{semantics.table}` ON ${semantics.table} (${semantics.semanticsid});
CREATE UNIQUE INDEX `uk_@{semantics.table}_@{semantics.semantics}` ON ${semantics.table} (${semantics.semantics});
