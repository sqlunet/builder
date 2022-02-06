ALTER TABLE ${members.table} ADD CONSTRAINT fk_${members.table}_${members.rolesetid} FOREIGN KEY (${members.rolesetid}) REFERENCES ${rolesets.table} (${rolesets.rolesetid});
ALTER TABLE ${members.table} ADD CONSTRAINT fk_${members.table}_${members.pbwordid} FOREIGN KEY (${members.pbwordid}) REFERENCES ${words.table} (${words.wordid});
