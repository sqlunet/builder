ALTER TABLE ${semantics.table} ADD CONSTRAINT `uniq_@{semantics.table}_@{semantics.semantics}` UNIQUE KEY (${semantics.semantics}(256));
