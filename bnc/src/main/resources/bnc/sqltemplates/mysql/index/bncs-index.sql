-- ALTER TABLE ${bncs.table} ADD CONSTRAINT `uk_@{bncs.table}_@{bncs.word}` UNIQUE KEY (${bncs.word});
ALTER TABLE ${bncs.table} ADD KEY `k_@{bncs.table}_@{bncs.wordid}` (${bncs.wordid});
ALTER TABLE ${bncs.table} ADD KEY `k_@{bncs.table}_@{bncs.word}` (${bncs.word});
-- ALTER TABLE ${bncs.table} ADD KEY `k_@{bncs.table}_@{bncs.pos}` (${bncs.pos});
