ALTER TABLE ${rolesets.table} ADD CONSTRAINT fk_${rolesets.table}_${rolesets.pbwordid} FOREIGN KEY (${rolesets.pbwordid}) REFERENCES ${pbword.table} (${pbword.pbwordid});
