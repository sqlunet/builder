ALTER TABLE ${semantics.table} ADD CONSTRAINT `pk_@{semantics.table}` PRIMARY KEY (${semantics.semanticsid});
ALTER TABLE ${semantics.table} ADD CONSTRAINT `uk_@{semantics.table}_@{semantics.semantics}` UNIQUE KEY (${semantics.semantics}(380));
