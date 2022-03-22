ALTER TABLE ${pmroles.table} ADD CONSTRAINT `pk_@{pmroles.table}` PRIMARY KEY (${pmroles.pmroleid});
ALTER TABLE ${pmroles.table} ADD CONSTRAINT `k_@{pmroles.table}_@{pmroles.predicate}_@{pmroles.role}_@{pmroles.pos}` UNIQUE KEY (${pmroles.predicate},${pmroles.role},${pmroles.pos});
