CREATE TABLE IF NOT EXISTS ${predicates_semantics.table} (
    ${predicates_semantics.predicateid} INTEGER NOT NULL,
    ${predicates_semantics.semanticsid} INTEGER NOT NULL,
PRIMARY KEY (${predicates_semantics.predicateid},${predicates_semantics.semanticsid}));
