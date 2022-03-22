ALTER TABLE ${predicates_semantics.table} ADD CONSTRAINT `pk_@{predicates_semantics.table}` PRIMARY KEY (${predicates_semantics.predicateid},${predicates_semantics.semanticsid});
ALTER TABLE ${predicates_semantics.table} ADD KEY `k_@{predicates_semantics.table}_@{predicates_semantics.semanticsid}` (${predicates_semantics.semanticsid});
ALTER TABLE ${predicates_semantics.table} ADD KEY `k_@{predicates_semantics.table}_@{predicates_semantics.predicateid}` (${predicates_semantics.predicateid});
