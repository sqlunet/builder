ALTER TABLE ${predicates.table} ADD CONSTRAINT `uk_@{predicates.table}_@{predicates.predicate}` UNIQUE KEY (${predicates.predicate});
