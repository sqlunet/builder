ALTER TABLE ${frames.table} ADD CONSTRAINT `pk_@{frames.table}` PRIMARY KEY (${frames.frameid});
-- ALTER TABLE ${frames.table} ADD CONSTRAINT `uk_@{frames.table}_@{frames.number}_@{frames.xtag}_@{frames.framenameid}_@{frames.framesubnameid}_@{frames.syntaxid}_@{frames.semanticsid}` UNIQUE KEY (${frames.number},${frames.xtag},${frames.framenameid},${frames.framesubnameid},${frames.syntaxid},${frames.semanticsid});
ALTER TABLE ${frames.table} ADD CONSTRAINT `uk_@{frames.table}_all` UNIQUE KEY (${frames.number},${frames.xtag},${frames.framenameid},${frames.framesubnameid},${frames.syntaxid},${frames.semanticsid});
ALTER TABLE ${frames.table} ADD KEY `k_@{frames.table}_@{frames.framenameid}` (${frames.framenameid});
ALTER TABLE ${frames.table} ADD KEY `k_@{frames.table}_@{frames.framesubnameid}` (${frames.framesubnameid});
ALTER TABLE ${frames.table} ADD KEY `k_@{frames.table}_@{frames.syntaxid}` (${frames.syntaxid});
ALTER TABLE ${frames.table} ADD KEY `k_@{frames.table}_@{frames.semanticsid}` (${frames.semanticsid});
