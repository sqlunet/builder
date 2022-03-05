CREATE TABLE IF NOT EXISTS ${grouppatterns_annosets.table} (
${grouppatterns_annosets.patternid} INTEGER NOT NULL,
${grouppatterns_annosets.annosetid} INTEGER NOT NULL,
PRIMARY KEY (${grouppatterns_annosets.patternid},${grouppatterns_annosets.annosetid}) );
