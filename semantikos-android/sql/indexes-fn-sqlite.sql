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
CREATE INDEX IF NOT EXISTS `k_fn_words_word` ON `fn_words`(`word`);
