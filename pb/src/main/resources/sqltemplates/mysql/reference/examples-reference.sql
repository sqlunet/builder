ALTER TABLE ${examples.table} ADD CONSTRAINT fk_${examples.table}_${examples.rolesetid} FOREIGN KEY (${examples.rolesetid}) REFERENCES ${rolesets.table} (${rolesets.rolesetid});
ALTER TABLE ${examples.table} ADD CONSTRAINT fk_${examples.table}_${examples.aspectid} FOREIGN KEY (${examples.aspectid}) REFERENCES ${aspects.table} (${aspects.aspectid});
ALTER TABLE ${examples.table} ADD CONSTRAINT fk_${examples.table}_${examples.formid} FOREIGN KEY (${examples.formid}) REFERENCES ${forms.table} (${forms.formid});
ALTER TABLE ${examples.table} ADD CONSTRAINT fk_${examples.table}_${examples.tenseid} FOREIGN KEY (${examples.tenseid}) REFERENCES ${tenses.table} (${tenses.tenseid});
ALTER TABLE ${examples.table} ADD CONSTRAINT fk_${examples.table}_${examples.voiceid} FOREIGN KEY (${examples.voiceid}) REFERENCES ${voices.table} (${voices.voiceid});
ALTER TABLE ${examples.table} ADD CONSTRAINT fk_${examples.table}_${examples.personid} FOREIGN KEY (${examples.personid}) REFERENCES ${persons.table} (${persons.personid});
