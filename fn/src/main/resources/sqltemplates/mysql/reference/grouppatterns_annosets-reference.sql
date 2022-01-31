ALTER TABLE ${grouppatterns_annosets.table} ADD CONSTRAINT fk_${grouppatterns_annosets.table}_${grouppatterns_annosets.patternid} FOREIGN KEY (${grouppatterns_annosets.patternid}) REFERENCES ${grouppatterns.table} (${grouppatterns.patternid});
ALTER TABLE ${grouppatterns_annosets.table} ADD CONSTRAINT fk_${grouppatterns_annosets.table}_${grouppatterns_annosets.annosetid} FOREIGN KEY (${grouppatterns_annosets.annosetid}) REFERENCES ${annosets.table} (${annosets.annosetid});
