CREATE INDEX IF NOT EXISTS k_${roles.table}_${roles.roledescr} ON ${roles.table} (${roles.roledescr});
CREATE INDEX IF NOT EXISTS k_${roles.table}_${roles.rolesetid} ON ${roles.table} (${roles.rolesetid});
