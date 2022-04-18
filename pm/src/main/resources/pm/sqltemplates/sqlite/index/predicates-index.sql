CREATE UNIQUE INDEX `pk_@{predicates.table}` ON ${predicates.table} (${predicates.predicateid});
CREATE UNIQUE INDEX `uk_@{predicates.table}_@{predicates.predicate}` ON ${predicates.table} (${predicates.predicate});
