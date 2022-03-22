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

CREATE INDEX IF NOT EXISTS index_syntagms_synset1id_word1id ON sn_syntagms(synset1id,word1id);
CREATE INDEX IF NOT EXISTS index_syntagms_synset2id_word2id ON sn_syntagms(synset2id,word2id);

