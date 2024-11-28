ALTER TABLE ${examples.table} ADD CONSTRAINT `fk_@{examples.table}_@{examples.rolesetid}` FOREIGN KEY (${examples.rolesetid}) REFERENCES ${rolesets.table} (${rolesets.rolesetid});
