ALTER TABLE ${predicates.table} ADD CONSTRAINT `uniq_@{predicates.table}_@{predicates.predicate}` UNIQUE KEY (${predicates.predicate});
