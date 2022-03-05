-- ALTER TABLE ${convtasks.table} ADD CONSTRAINT `k_@{convtasks.table}_@{convtasks.word}` UNIQUE KEY (${convtasks.word});
ALTER TABLE ${convtasks.table} ADD KEY `k_@{convtasks.table}_@{convtasks.wordid}` (${convtasks.wordid});
ALTER TABLE ${convtasks.table} ADD KEY `k_@{convtasks.table}_@{convtasks.word}` (${convtasks.word});
-- ALTER TABLE ${convtasks.table} ADD KEY `k_@{convtasks.table}_@{convtasks.pos}` (${convtasks.pos});
