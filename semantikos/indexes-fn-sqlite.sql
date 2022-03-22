CREATE INDEX IF NOT EXISTS index_fn_words_wordid ON fn_words(wordid);
CREATE INDEX IF NOT EXISTS index_fn_frames_related_frameid ON fn_frames_related(frameid);
CREATE INDEX IF NOT EXISTS index_fn_frames_related_frame2id ON fn_frames_related(frame2id);
CREATE INDEX IF NOT EXISTS index_fn_lexemes_fnwordid ON fn_lexemes(fnwordid);
CREATE INDEX IF NOT EXISTS index_fn_fes_frameid ON fn_fes(frameid);
CREATE INDEX IF NOT EXISTS index_fn_ferealizations_luid ON fn_ferealizations(luid);
CREATE INDEX IF NOT EXISTS index_fn_fegrouprealizations_luid ON fn_fegrouprealizations(luid);
CREATE INDEX IF NOT EXISTS index_fn_grouppatterns_fegrid ON fn_grouppatterns(fegrid);
CREATE INDEX IF NOT EXISTS index_fn_grouppatterns_patterns_patternid ON fn_grouppatterns_patterns(patternid);
CREATE INDEX IF NOT EXISTS index_fn_ferealizations_valenceunits_ferid ON fn_ferealizations_valenceunits(ferid);
CREATE INDEX IF NOT EXISTS index_fn_ferealizations_valenceunits_vuid ON fn_ferealizations_valenceunits(vuid);
CREATE INDEX IF NOT EXISTS index_fn_valenceunits_vuid ON fn_valenceunits(vuid);
CREATE INDEX IF NOT EXISTS index_fn_subcorpuses_luid ON fn_subcorpuses (luid);
CREATE INDEX IF NOT EXISTS index_fn_annosets_sentenceid ON fn_annosets (sentenceid);
CREATE INDEX IF NOT EXISTS index_fn_layers_annosetid ON fn_layers (annosetid);
CREATE INDEX IF NOT EXISTS index_fn_labels_layerid ON fn_labels(layerid);

