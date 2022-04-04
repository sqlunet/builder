CREATE INDEX IF NOT EXISTS `k_syntagms_synset1id` ON `sn_syntagms`(`synset1id`);
CREATE INDEX IF NOT EXISTS `k_syntagms_word1id` ON `sn_syntagms`(`word1id`);
CREATE INDEX IF NOT EXISTS `k_syntagms_synset2id` ON `sn_syntagms`(`synset2id`);
CREATE INDEX IF NOT EXISTS `k_syntagms_word2id` ON `sn_syntagms`(`word2id`);

CREATE INDEX IF NOT EXISTS `k_words_word` ON `words`(`word`);
CREATE INDEX IF NOT EXISTS `k_casedwords_wordid_casedwordid` ON `casedwords`(`wordid`,`casedwordid`);

CREATE INDEX IF NOT EXISTS `k_senses_wordid` ON `senses`(`wordid`);
CREATE INDEX IF NOT EXISTS `k_senses_synsetid` ON `senses`(`synsetid`);

CREATE INDEX IF NOT EXISTS `k_synsets_synsetid` ON `synsets`(`synsetid`);

CREATE INDEX IF NOT EXISTS `k_semrelations_synset1id` ON `semrelations`(`synset1id`);
CREATE INDEX IF NOT EXISTS `k_semrelations_relationid` ON `semrelations`(`relationid`);

CREATE INDEX IF NOT EXISTS `k_lexrelations_synset1id` ON `lexrelations`(`synset1id`);
CREATE INDEX IF NOT EXISTS `k_lexrelations_word1id` ON `lexrelations`(`synset1id`);
CREATE INDEX IF NOT EXISTS `k_lexrelations_relationid` ON `lexrelations`(`relationid`);

CREATE INDEX IF NOT EXISTS `k_samples_synsetid` ON `samples`(`synsetid`);
