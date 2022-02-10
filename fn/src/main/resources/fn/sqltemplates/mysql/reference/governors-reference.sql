ALTER TABLE ${governors.table} ADD CONSTRAINT `fk_@{governors.table}_@{governors.fnwordid}` FOREIGN KEY (${governors.fnwordid}) REFERENCES ${words.table} (${words.fnwordid});
