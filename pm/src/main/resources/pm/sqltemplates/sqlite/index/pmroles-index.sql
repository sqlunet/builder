CREATE UNIQUE INDEX `pk_@{pmroles.table}` ON ${pmroles.table} (${pmroles.pmroleid});
CREATE UNIQUE INDEX `k_@{pmroles.table}_@{pmroles.predicate}_@{pmroles.role}_@{pmroles.pos}` ON ${pmroles.table} (${pmroles.predicate},${pmroles.role},${pmroles.pos});
