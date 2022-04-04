CREATE INDEX IF NOT EXISTS `pk_words` ON `words` (`wordid`);
CREATE INDEX IF NOT EXISTS `k_words_word` ON `words`(`word`);

CREATE INDEX IF NOT EXISTS `pk_casedwords` ON `casedwords` (`casedwordid`);
CREATE INDEX IF NOT EXISTS `k_casedwords_wordid` ON `casedwords`(`wordid`);

CREATE INDEX IF NOT EXISTS `pk_senses` ON `senses` (`senseid`);
CREATE INDEX IF NOT EXISTS `k_senses_wordid` ON `senses`(`wordid`);
CREATE INDEX IF NOT EXISTS `k_senses_synsetid` ON `senses`(`synsetid`);

CREATE INDEX IF NOT EXISTS `pk_synsets` ON `synsets` (`synsetid`);

CREATE INDEX IF NOT EXISTS `k_semrelations_synset1id` ON `semrelations`(`synset1id`);
CREATE INDEX IF NOT EXISTS `k_semrelations_relationid` ON `semrelations`(`relationid`);

CREATE INDEX IF NOT EXISTS `k_lexrelations_synset1id` ON `lexrelations`(`synset1id`);
CREATE INDEX IF NOT EXISTS `k_lexrelations_word1id` ON `lexrelations`(`synset1id`);
CREATE INDEX IF NOT EXISTS `k_lexrelations_relationid` ON `lexrelations`(`relationid`);

CREATE INDEX IF NOT EXISTS `pk_relations` ON `relations` (`relationid`);

CREATE INDEX IF NOT EXISTS `k_samples_synsetid` ON samples(synsetid);

CREATE INDEX IF NOT EXISTS `pk_lexes` ON `lexes` (`luid`);

CREATE INDEX IF NOT EXISTS `pk_domains` ON `domains` (`domainid`);
CREATE INDEX IF NOT EXISTS `pk_morphs` ON `morphs` (`morphid`);
CREATE INDEX IF NOT EXISTS `pk_pronunciations` ON `pronunciations` (`pronunciationid`);
CREATE INDEX IF NOT EXISTS `pk_samples` ON `samples` (`synsetid`,`sampleid`);
CREATE INDEX IF NOT EXISTS `pk_vframes` ON `vframes` (`frameid`);
CREATE INDEX IF NOT EXISTS `pk_vtemplates` ON `vtemplates` (`templateid`);

CREATE INDEX IF NOT EXISTS `pk_lexes_morphs_wordid` ON `lexes_morphs` (`wordid`);
