-- wordnet

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

-- bnc

CREATE INDEX IF NOT EXISTS `k_bnc_bncs_wordid_posid` ON `bnc_bncs` (`wordid`,`posid`);
CREATE INDEX IF NOT EXISTS `k_bnc_spwrs_wordid_posid` ON `bnc_spwrs` (`wordid`,`posid`);
CREATE INDEX IF NOT EXISTS `k_bnc_convtasks_wordid_posid` ON `bnc_convtasks` (`wordid`,`posid`);
CREATE INDEX IF NOT EXISTS `k_bnc_imaginfs_wordid_posid` ON `bnc_imaginfs` (`wordid`,`posid`);

-- syntagnet

CREATE INDEX IF NOT EXISTS `k_sn_syntagms_synset1id` ON `sn_syntagms` (`synset1id`);
CREATE INDEX IF NOT EXISTS `k_sn_syntagms_word1id` ON `sn_syntagms` (`word1id`);
CREATE INDEX IF NOT EXISTS `k_sn_syntagms_synset2id` ON `sn_syntagms` (`synset2id`);
CREATE INDEX IF NOT EXISTS `k_sn_syntagms_word2id` ON `sn_syntagms` (`word2id`);

-- verbnet

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
CREATE INDEX IF NOT EXISTS `k_vn_members_groupings_classid` ON `vn_members_groupings` (`classid`);
CREATE INDEX IF NOT EXISTS `k_vn_members_groupings_vnwordid` ON `vn_members_groupings` (`vnwordid`);
CREATE INDEX IF NOT EXISTS `k_vn_members_senses_vnwordid` ON `vn_members_senses` (`vnwordid`);
CREATE INDEX IF NOT EXISTS `pk_vn_groupings` ON `vn_groupings` (`groupingid`);
CREATE INDEX IF NOT EXISTS `pk_vn_words` ON `vn_words` (`vnwordid`);
CREATE INDEX IF NOT EXISTS `k_vn_words_wordid` ON `vn_words`(`wordid`);

-- propbank

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
CREATE INDEX IF NOT EXISTS `pk_pb_argtypes` ON `pb_argtypes` (`argtypeid`);
CREATE INDEX IF NOT EXISTS `pk_pb_aspects` ON `pb_aspects` (`aspectid`);
CREATE INDEX IF NOT EXISTS `pk_pb_forms` ON `pb_forms` (`formid`);
CREATE INDEX IF NOT EXISTS `pk_pb_funcs` ON `pb_funcs` (`funcid`);
CREATE INDEX IF NOT EXISTS `pk_pb_persons` ON `pb_persons` (`personid`);
CREATE INDEX IF NOT EXISTS `pk_pb_tenses` ON `pb_tenses` (`tenseid`);
CREATE INDEX IF NOT EXISTS `pk_pb_voices` ON `pb_voices` (`voiceid`);
CREATE INDEX IF NOT EXISTS `k_pb_members_rolesetid` ON `pb_members` (`rolesetid`);
CREATE INDEX IF NOT EXISTS `pk_pb_words` ON `pb_words` (`pbwordid`);
CREATE INDEX IF NOT EXISTS `k_pb_words_wordid` ON `pb_words` (`wordid`);

-- fn

CREATE INDEX IF NOT EXISTS `pk_fn_lexunits_governors` ON `fn_lexunits_governors` (`luid`,`governorid`);
CREATE INDEX IF NOT EXISTS `pk_fn_lexunits` ON `fn_lexunits` (`luid`);
CREATE INDEX IF NOT EXISTS `pk_fn_lexunits_semtypes` ON `fn_lexunits_semtypes` (`luid`,`semtypeid`);

CREATE INDEX IF NOT EXISTS `pk_fn_lexemes` ON `fn_lexemes` (`lexemeid`);
CREATE INDEX IF NOT EXISTS `k_fn_lexemes_fnwordid` ON `fn_lexemes`(`fnwordid`);

CREATE INDEX IF NOT EXISTS `pk_fn_frames` ON `fn_frames` (`frameid`);
CREATE INDEX IF NOT EXISTS `pk_fn_framerelations` ON `fn_framerelations` (`relationid`);
CREATE INDEX IF NOT EXISTS `pk_fn_frames_related` ON `fn_frames_related` (`frameid`,`frame2id`,`relationid`);
CREATE INDEX IF NOT EXISTS `k_fn_frames_related_frameid` ON `fn_frames_related`(`frameid`);
CREATE INDEX IF NOT EXISTS `k_fn_frames_related_frame2id` ON `fn_frames_related`(`frame2id`);
CREATE INDEX IF NOT EXISTS `pk_fn_frames_semtypes` ON `fn_frames_semtypes` (`frameid`,`semtypeid`);
CREATE INDEX IF NOT EXISTS `pk_fn_fes_excluded` ON `fn_fes_excluded` (`feid`,`fe2id`);
CREATE INDEX IF NOT EXISTS `pk_fn_fes_required` ON `fn_fes_required` (`feid`,`fe2id`);

CREATE INDEX IF NOT EXISTS `pk_fn_fes` ON `fn_fes` (`feid`);
CREATE INDEX IF NOT EXISTS `k_fn_fes_frameid` ON `fn_fes`(`frameid`);
CREATE INDEX IF NOT EXISTS `pk_fn_fes_semtypes` ON `fn_fes_semtypes` (`feid`,`semtypeid`);
CREATE INDEX IF NOT EXISTS `pk_fn_fes_fegrouprealizations` ON `fn_fes_fegrouprealizations` (`feid`,`fetypeid`,`fegrid`);

CREATE INDEX IF NOT EXISTS `pk_fn_poses` ON `fn_poses` (`posid`);
CREATE INDEX IF NOT EXISTS `pk_fn_coretypes` ON `fn_coretypes` (`coretypeid`);
CREATE INDEX IF NOT EXISTS `pk_fn_fetypes` ON `fn_fetypes` (`fetypeid`);
CREATE INDEX IF NOT EXISTS `pk_fn_semtypes` ON `fn_semtypes` (`semtypeid`);
CREATE INDEX IF NOT EXISTS `pk_fn_layertypes` ON `fn_layertypes` (`layertypeid`);
CREATE INDEX IF NOT EXISTS `pk_fn_labeltypes` ON `fn_labeltypes` (`labeltypeid`);
CREATE INDEX IF NOT EXISTS `pk_fn_labelitypes` ON `fn_labelitypes` (`labelitypeid`);
CREATE INDEX IF NOT EXISTS `pk_fn_pttypes` ON `fn_pttypes` (`ptid`);
CREATE INDEX IF NOT EXISTS `pk_fn_gftypes` ON `fn_gftypes` (`gfid`);

CREATE INDEX IF NOT EXISTS `pk_fn_governors` ON `fn_governors` (`governorid`);
CREATE INDEX IF NOT EXISTS `pk_fn_annosets` ON `fn_annosets` (`annosetid`);
CREATE INDEX IF NOT EXISTS `k_fn_annosets_sentenceid` ON `fn_annosets` (`sentenceid`);
CREATE INDEX IF NOT EXISTS `pk_fn_sentences` ON `fn_sentences` (`sentenceid`);
CREATE INDEX IF NOT EXISTS `pk_fn_valenceunits` ON `fn_valenceunits` (`vuid`);
CREATE INDEX IF NOT EXISTS `pk_fn_layers` ON `fn_layers` (`layerid`);
CREATE INDEX IF NOT EXISTS `k_fn_layers_annosetid` ON `fn_layers` (`annosetid`);
CREATE INDEX IF NOT EXISTS `pk_fn_labels` ON `fn_labels` (`labelid`);
CREATE INDEX IF NOT EXISTS `k_fn_labels_layerid` ON `fn_labels`(`layerid`);

CREATE INDEX IF NOT EXISTS `pk_fn_semtypes_supers` ON `fn_semtypes_supers` (`semtypeid`,`supersemtypeid`);

CREATE INDEX IF NOT EXISTS `pk_fn_corpuses` ON `fn_corpuses` (`corpusid`);
CREATE INDEX IF NOT EXISTS `pk_fn_documents` ON `fn_documents` (`documentid`);
CREATE INDEX IF NOT EXISTS `pk_fn_subcorpuses` ON `fn_subcorpuses` (`subcorpusid`);
CREATE INDEX IF NOT EXISTS `k_fn_subcorpuses_luid` ON `fn_subcorpuses` (`luid`);
CREATE INDEX IF NOT EXISTS `pk_fn_subcorpuses_sentences` ON `fn_subcorpuses_sentences` (`subcorpusid`,`sentenceid`);

CREATE INDEX IF NOT EXISTS `pk_fn_ferealizations` ON `fn_ferealizations` (`ferid`);
CREATE INDEX IF NOT EXISTS `k_fn_ferealizations_luid` ON `fn_ferealizations`(`luid`);
CREATE INDEX IF NOT EXISTS `k_fn_ferealizations_valenceunits_ferid` ON `fn_ferealizations_valenceunits`(`ferid`);
CREATE INDEX IF NOT EXISTS `k_fn_ferealizations_valenceunits_vuid` ON `fn_ferealizations_valenceunits`(`vuid`);
CREATE INDEX IF NOT EXISTS `pk_fn_fegrouprealizations` ON `fn_fegrouprealizations` (`fegrid`);
CREATE INDEX IF NOT EXISTS `k_fn_fegrouprealizations_luid` ON `fn_fegrouprealizations`(`luid`);
CREATE INDEX IF NOT EXISTS `pk_fn_grouppatterns` ON `fn_grouppatterns` (`patternid`);
CREATE INDEX IF NOT EXISTS `k_fn_grouppatterns_patterns_patternid` ON `fn_grouppatterns_patterns`(`patternid`);
CREATE INDEX IF NOT EXISTS `k_fn_grouppatterns_fegrid` ON `fn_grouppatterns`(`fegrid`);
CREATE INDEX IF NOT EXISTS `pk_fn_grouppatterns_annosets` ON `fn_grouppatterns_annosets` (`patternid`,`annosetid`);
CREATE INDEX IF NOT EXISTS `pk_fn_governors_annosets` ON `fn_governors_annosets` (`governorid`,`annosetid`);
CREATE INDEX IF NOT EXISTS `pk_fn_valenceunits_annosets` ON `fn_valenceunits_annosets` (`vuid`,`annosetid`);
CREATE INDEX IF NOT EXISTS `k_fn_valenceunits_vuid` ON `fn_valenceunits`(`vuid`);

CREATE INDEX IF NOT EXISTS `pk_fn_words` ON `fn_words` (`fnwordid`);
-- CREATE INDEX IF NOT EXISTS `k_fn_words_word` ON `fn_words`(`word`);

-- pm
CREATE INDEX IF NOT EXISTS `k_pm_pms_wordid` ON `pm_pms` (`wordid`);
CREATE INDEX IF NOT EXISTS `k_pm_pms_synsetid` ON `pm_pms` (`synsetid`);
CREATE INDEX IF NOT EXISTS `k_pm_pms_word` ON `pm_pms` (`word`);

