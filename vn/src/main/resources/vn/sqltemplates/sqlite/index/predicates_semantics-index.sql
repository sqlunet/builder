CREATE UNIQUE INDEX `pk_@{predicates_semantics.table}` ON ${predicates_semantics.table} (${predicates_semantics.predicateid},${predicates_semantics.semanticsid});
CREATE INDEX `k_@{predicates_semantics.table}_@{predicates_semantics.semanticsid}` ON ${predicates_semantics.table} (${predicates_semantics.semanticsid});
CREATE INDEX `k_@{predicates_semantics.table}_@{predicates_semantics.predicateid}` ON ${predicates_semantics.table} (${predicates_semantics.predicateid});
