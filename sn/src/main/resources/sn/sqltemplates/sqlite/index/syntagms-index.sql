CREATE UNIQUE INDEX `pk_@{syntagms.table}` ON `sn_syntagms` (${syntagms.syntagmid});
CREATE INDEX `k_@{syntagms.table}_@{syntagms.synset1id}_@{syntagms.word1id}` ON `sn_syntagms` (${syntagms.synset1id},${syntagms.word1id});
CREATE INDEX `k_@{syntagms.table}_@{syntagms.synset2id}_@{syntagms.word2id}` ON `sn_syntagms` (${syntagms.synset2id},${syntagms.word2id});
CREATE INDEX `k_@{syntagms.table}_@{syntagms.sensekey1}` ON `sn_syntagms` (${syntagms.sensekey1});
CREATE INDEX `k_@{syntagms.table}_@{syntagms.sensekey2}` ON `sn_syntagms` (${syntagms.sensekey2});
