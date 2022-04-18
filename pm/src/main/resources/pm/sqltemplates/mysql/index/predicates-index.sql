ALTER TABLE ${predicates.table} ADD CONSTRAINT `pk_@{predicates.table}` PRIMARY KEY (${predicates.predicateid});
ALTER TABLE ${predicates.table} ADD CONSTRAINT `uk_@{predicates.table}_@{predicates.predicate}` UNIQUE KEY (${predicates.predicate});
