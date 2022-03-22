CREATE INDEX IF NOT EXISTS index_words_word ON words(word);
CREATE INDEX IF NOT EXISTS index_casedwords_wordid_casedwordid ON casedwords(wordid,casedwordid);

CREATE INDEX IF NOT EXISTS index_senses_wordid ON senses(wordid);
CREATE INDEX IF NOT EXISTS index_senses_synsetid ON senses(synsetid);

CREATE INDEX IF NOT EXISTS index_synsets_synsetid ON synsets(synsetid);

CREATE INDEX IF NOT EXISTS index_semrelations_synset1id ON semrelations(synset1id);
CREATE INDEX IF NOT EXISTS index_semrelations_relationid ON semrelations(relationid);

CREATE INDEX IF NOT EXISTS index_lexrelations_synset1id ON lexrelations(synset1id);
CREATE INDEX IF NOT EXISTS index_lexrelations_word1id ON lexrelations(synset1id);
CREATE INDEX IF NOT EXISTS index_lexrelations_relationid ON lexrelations(relationid);

CREATE INDEX IF NOT EXISTS index_samples_synsetid ON samples(synsetid);

CREATE INDEX IF NOT EXISTS index_vn_words_wordid ON vn_words(wordid);
CREATE INDEX IF NOT EXISTS index_vn_roles_classid ON vn_roles(classid);
CREATE INDEX IF NOT EXISTS index_vn_classes_frames_classid ON vn_classes_frames(classid);

CREATE INDEX IF NOT EXISTS index_pb_words_wordid ON pb_words(wordid);
CREATE INDEX IF NOT EXISTS index_pb_rolesets_pbwordid ON pb_rolesets(pbwordid);
CREATE INDEX IF NOT EXISTS index_pb_roles_rolesetid ON pb_roles(rolesetid);
CREATE INDEX IF NOT EXISTS index_pb_examples_rolesetid ON pb_examples(rolesetid);
CREATE INDEX IF NOT EXISTS index_pb_rels_exampleid ON pb_rels(exampleid);
CREATE INDEX IF NOT EXISTS index_pb_args_exampleid ON pb_args(exampleid);

