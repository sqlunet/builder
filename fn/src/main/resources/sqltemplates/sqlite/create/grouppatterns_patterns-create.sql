CREATE TABLE IF NOT EXISTS ${grouppatterns_patterns.table} (
    ${grouppatterns_patterns.patternid} INTEGER NOT NULL,
    ${grouppatterns_patterns.ferid} INTEGER NOT NULL,
    ${grouppatterns_patterns.vuid} INTEGER NOT NULL,
PRIMARY KEY (${grouppatterns_patterns.ferid},${grouppatterns_patterns.patternid},${grouppatterns_patterns.vuid}) );
