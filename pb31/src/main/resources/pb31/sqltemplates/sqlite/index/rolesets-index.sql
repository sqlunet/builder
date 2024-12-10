CREATE UNIQUE INDEX `pk_@{rolesets.table}` ON ${rolesets.table} (${rolesets.rolesetid});
CREATE INDEX `k_@{rolesets.table}_@{rolesets.rolesethead}` ON ${rolesets.table} (${rolesets.rolesethead});
CREATE INDEX `k_@{rolesets.table}_@{rolesets.rolesetname}` ON ${rolesets.table} (${rolesets.rolesetname});
CREATE INDEX `k_@{rolesets.table}_@{rolesets.pbwordid}` ON ${rolesets.table} (${rolesets.pbwordid});
