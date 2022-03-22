ALTER TABLE ${framesubnames.table} ADD CONSTRAINT `pk_@{framesubnames.table}` PRIMARY KEY (${framesubnames.framesubnameid});
ALTER TABLE ${framesubnames.table} ADD CONSTRAINT `uk_@{framesubnames.table}_@{framesubnames.framesubname}` UNIQUE KEY (${framesubnames.framesubname});
