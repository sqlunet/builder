CREATE UNIQUE INDEX `pk_@{frames.table}` ON ${frames.table} (${frames.frameid});
CREATE UNIQUE INDEX `uk_@{frames.table}_all` ON ${frames.table} (${frames.number},${frames.xtag},${frames.framenameid},${frames.framesubnameid},${frames.syntaxid},${frames.semanticsid});
CREATE INDEX `k_@{frames.table}_@{frames.framenameid}` ON ${frames.table} (${frames.framenameid});
CREATE INDEX `k_@{frames.table}_@{frames.framesubnameid}` ON ${frames.table} (${frames.framesubnameid});
CREATE INDEX `k_@{frames.table}_@{frames.syntaxid}` ON ${frames.table} (${frames.syntaxid});
CREATE INDEX `k_@{frames.table}_@{frames.semanticsid}` ON ${frames.table} (${frames.semanticsid});
