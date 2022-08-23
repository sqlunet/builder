ALTER TABLE ${roles.table} ADD CONSTRAINT `fk_@{roles.table}_@{roles.predicateid}` FOREIGN KEY (${roles.predicateid}) REFERENCES ${predicates.table} (${predicates.predicateid});
