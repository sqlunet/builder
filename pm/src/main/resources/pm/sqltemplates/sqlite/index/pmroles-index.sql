ALTER TABLE ${pmroles.table} ADD CONSTRAINT UNIQUE KEY `k_@{pmroles.table}_@{pmroles.predicate}_@{pmroles.role}_@{pmroles.pos}` (${pmroles.predicate},${pmroles.role},${pmroles.pos});
