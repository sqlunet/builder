ALTER TABLE ${examples.table} ADD CONSTRAINT fk_${examples.table}_${examples.rolesetid} FOREIGN KEY (${examples.rolesetid}) REFERENCES ${rolesets.table} (${rolesets.rolesetid});
ALTER TABLE ${examples.table} ADD CONSTRAINT fk_${examples.table}_${examples.aspect} FOREIGN KEY (${examples.aspect}) REFERENCES ${aspects.table} (${aspects.aspect});
ALTER TABLE ${examples.table} ADD CONSTRAINT fk_${examples.table}_${examples.form} FOREIGN KEY (${examples.form}) REFERENCES ${forms.table} (${forms.form});
ALTER TABLE ${examples.table} ADD CONSTRAINT fk_${examples.table}_${examples.tense} FOREIGN KEY (${examples.tense}) REFERENCES ${tenses.table} (${tenses.tense});
ALTER TABLE ${examples.table} ADD CONSTRAINT fk_${examples.table}_${examples.voice} FOREIGN KEY (${examples.voice}) REFERENCES ${voices.table} (${voices.voice});
ALTER TABLE ${examples.table} ADD CONSTRAINT fk_${examples.table}_${examples.person} FOREIGN KEY (${examples.person}) REFERENCES ${persons.table} (${persons.person});
