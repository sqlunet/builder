CREATE TABLE IF NOT EXISTS ${patterns_annosets.table} (
    ${patterns_annosets.patternid} INTEGER NOT NULL,
    ${patterns_annosets.annosetid} INTEGER NOT NULL,
PRIMARY KEY (${patterns_annosets.patternid},${patterns_annosets.annosetid}) );
