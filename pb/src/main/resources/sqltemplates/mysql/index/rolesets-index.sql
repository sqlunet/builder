CREATE INDEX IF NOT EXISTS k_${rolesets.table}_${rolesets.rolesethead} ON ${rolesets.table} (${rolesets.rolesethead});
CREATE INDEX IF NOT EXISTS k_${rolesets.table}_${rolesets.rolesetname} ON ${rolesets.table} (${rolesets.rolesetname});
CREATE INDEX IF NOT EXISTS k_${rolesets.table}_${rolesets.pbwordid} ON ${rolesets.table} (${rolesets.pbwordid});
