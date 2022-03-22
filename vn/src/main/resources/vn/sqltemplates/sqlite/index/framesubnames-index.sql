CREATE UNIQUE INDEX `pk_@{framesubnames.table}` ON ${framesubnames.table} (${framesubnames.framesubnameid});
CREATE UNIQUE INDEX `uk_@{framesubnames.table}_@{framesubnames.framesubname}` ON ${framesubnames.table} (${framesubnames.framesubname});
