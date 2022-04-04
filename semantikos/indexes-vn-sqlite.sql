CREATE INDEX IF NOT EXISTS `pk_vn_classes` ON `vn_classes` (`classid`);
CREATE INDEX IF NOT EXISTS `k_vn_classes_frames_classid` ON `vn_classes_frames`(`classid`);
CREATE INDEX IF NOT EXISTS `k_vn_roles_classid` ON `vn_roles`(`classid`);
CREATE INDEX IF NOT EXISTS `pk_vn_roles` ON `vn_roles` (`roleid`);
CREATE INDEX IF NOT EXISTS `pk_vn_roletypes` ON `vn_roletypes` (`roletypeid`);
CREATE INDEX IF NOT EXISTS `pk_vn_restrs` ON `vn_restrs` (`restrsid`);
CREATE INDEX IF NOT EXISTS `pk_vn_restrtypes` ON `vn_restrtypes` (`restrid`);
CREATE INDEX IF NOT EXISTS `pk_vn_framenames` ON `vn_framenames` (`framenameid`);
CREATE INDEX IF NOT EXISTS `pk_vn_frames` ON `vn_frames` (`frameid`);
CREATE INDEX IF NOT EXISTS `pk_vn_framesubnames` ON `vn_framesubnames` (`framesubnameid`);
CREATE INDEX IF NOT EXISTS `pk_vn_frames_examples` ON `vn_frames_examples` (`frameid`,`exampleid`);
CREATE INDEX IF NOT EXISTS `pk_vn_examples` ON `vn_examples` (`exampleid`);
CREATE INDEX IF NOT EXISTS `pk_vn_predicates` ON `vn_predicates` (`predicateid`);
CREATE INDEX IF NOT EXISTS `pk_vn_predicates_semantics` ON `vn_predicates_semantics` (`predicateid`,`semanticsid`);
CREATE INDEX IF NOT EXISTS `pk_vn_semantics` ON `vn_semantics` (`semanticsid`);
CREATE INDEX IF NOT EXISTS `pk_vn_syntaxes` ON `vn_syntaxes` (`syntaxid`);
CREATE INDEX IF NOT EXISTS `pk_vn_members` ON `vn_members` (`classid`,`vnwordid`);
CREATE INDEX IF NOT EXISTS `pk_vn_groupings` ON `vn_groupings` (`groupingid`);
CREATE INDEX IF NOT EXISTS `pk_vn_words` ON `vn_words` (`vnwordid`);
CREATE INDEX IF NOT EXISTS `k_vn_words_wordid` ON `vn_words`(`wordid`);

CREATE INDEX IF NOT EXISTS `pk_pb_rolesets` ON `pb_rolesets` (`rolesetid`);
CREATE INDEX IF NOT EXISTS `pk_pb_roles` ON `pb_roles` (`roleid`);
CREATE INDEX IF NOT EXISTS `k_pb_roles_rolesetid` ON `pb_roles`(`rolesetid`);
CREATE INDEX IF NOT EXISTS `k_pb_rels_exampleid` ON `pb_rels`(`exampleid`);
CREATE INDEX IF NOT EXISTS `k_pb_args_exampleid` ON `pb_args`(`exampleid`);
CREATE INDEX IF NOT EXISTS `k_pb_examples_rolesetid` ON `pb_examples`(`rolesetid`);
CREATE INDEX IF NOT EXISTS `pk_pb_examples` ON `pb_examples` (`exampleid`);
CREATE INDEX IF NOT EXISTS `k_pb_rolesets_pbwordid` ON `pb_rolesets`(`pbwordid`);

CREATE INDEX IF NOT EXISTS `pk_pb_rels` ON `pb_rels` (`relid`);
CREATE INDEX IF NOT EXISTS `pk_pb_args` ON `pb_args` (`argid`);
CREATE INDEX IF NOT EXISTS `pk_pb_thetas` ON `pb_thetas` (`thetaid`);
CREATE INDEX IF NOT EXISTS `pk_pb_argns` ON `pb_argns` (`nargid`);
CREATE INDEX IF NOT EXISTS `pk_pb_aspects` ON `pb_aspects` (`aspectid`);
CREATE INDEX IF NOT EXISTS `pk_pb_forms` ON `pb_forms` (`formid`);
CREATE INDEX IF NOT EXISTS `pk_pb_funcs` ON `pb_funcs` (`funcid`);
CREATE INDEX IF NOT EXISTS `pk_pb_persons` ON `pb_persons` (`personid`);
CREATE INDEX IF NOT EXISTS `pk_pb_tenses` ON `pb_tenses` (`tenseid`);
CREATE INDEX IF NOT EXISTS `pk_pb_voices` ON `pb_voices` (`voiceid`);

CREATE INDEX IF NOT EXISTS `pk_pb_members` ON `pb_members` (`rolesetid`,`pbwordid`);
CREATE INDEX IF NOT EXISTS `pk_pb_words` ON `pb_words` (`pbwordid`);
CREATE INDEX IF NOT EXISTS `k_pb_words_wordid` ON `pb_words`(`wordid`);

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
