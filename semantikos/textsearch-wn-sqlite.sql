DROP TABLE IF EXISTS words_word_fts4;
CREATE VIRTUAL TABLE IF NOT EXISTS words_word_fts4 USING fts4(wordid INTEGER not null, word);
INSERT INTO words_word_fts4 SELECT wordid, word from words;

DROP TABLE IF EXISTS synsets_definition_fts4;
CREATE VIRTUAL TABLE IF NOT EXISTS synsets_definition_fts4 USING fts4(synsetid INTEGER not null, definition);
INSERT INTO synsets_definition_fts4 SELECT synsetid, definition from synsets;

DROP TABLE IF EXISTS samples_sample_fts4;
CREATE VIRTUAL TABLE IF NOT EXISTS samples_sample_fts4 USING fts4(synsetid INTEGER not null, sample);
INSERT INTO samples_sample_fts4 SELECT synsetid, sample FROM samples;
