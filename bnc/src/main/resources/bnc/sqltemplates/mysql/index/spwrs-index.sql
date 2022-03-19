-- ALTER TABLE ${spwrs.table} ADD CONSTRAINT `k_@{spwrs.table}_@{spwrs.word}` UNQUE KEY (${spwrs.word});
ALTER TABLE ${spwrs.table} ADD KEY `k_@{spwrs.table}_@{spwrs.wordid}` (${spwrs.wordid});
ALTER TABLE ${spwrs.table} ADD KEY `k_@{spwrs.table}_@{spwrs.word}` (${spwrs.word});
-- ALTER TABLE ${spwrs.table} ADD KEY `k_@{spwrs.table}_@{spwrs.posid}` (${spwrs.posid});
