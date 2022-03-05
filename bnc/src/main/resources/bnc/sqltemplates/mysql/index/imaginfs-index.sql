-- ALTER TABLE ${imaginfs.table} ADD CONSTRAINT `k_@{imaginfs.table}_@{imaginfs.word}` UNIQUE KEY (${imaginfs.word});
ALTER TABLE ${imaginfs.table} ADD KEY `k_@{imaginfs.table}_@{imaginfs.wordid}` (${imaginfs.wordid});
ALTER TABLE ${imaginfs.table} ADD KEY `k_@{imaginfs.table}_@{imaginfs.word}` (${imaginfs.word});
-- ALTER TABLE ${imaginfs.table} ADD KEY `k_@{imaginfs.table}_@{imaginfs.pos}` (${imaginfs.pos});
