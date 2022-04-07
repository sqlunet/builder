DROP TABLE IF EXISTS vn_examples_example_fts4;
CREATE VIRTUAL TABLE IF NOT EXISTS vn_examples_example_fts4 USING fts4(exampleid INTEGER not null, frameid INTEGER, classid INTEGER, example);
INSERT INTO vn_examples_example_fts4 SELECT exampleid, frameid, classid, example FROM vn_examples INNER JOIN vn_frames_examples USING (exampleid) INNER JOIN vn_frames USING (frameid) INNER JOIN vn_classes_frames USING (frameid) INNER JOIN vn_classes USING (classid);

DROP TABLE IF EXISTS pb_examples_text_fts4;
CREATE VIRTUAL TABLE IF NOT EXISTS pb_examples_text_fts4 USING fts4(exampleid INTEGER not null, rolesetid INTEGER not null, text);
INSERT INTO pb_examples_text_fts4 SELECT exampleid, rolesetid, text from pb_examples;

DROP TABLE IF EXISTS vn_words_word_fts4;
CREATE VIRTUAL TABLE IF NOT EXISTS vn_words_word_fts4 USING fts4(vnwordid INTEGER not null, word);
INSERT INTO vn_words_word_fts4 SELECT vnwordid, word from vn_words INNER JOIN words USING (wordid);

DROP TABLE IF EXISTS pb_words_word_fts4;
CREATE VIRTUAL TABLE IF NOT EXISTS pb_words_word_fts4 USING fts4(pbwordid INTEGER not null, word);
INSERT INTO pb_words_word_fts4 SELECT pbwordid, word from pb_words INNER JOIN words USING (wordid);

DROP TABLE IF EXISTS words_word_fts4;
CREATE VIRTUAL TABLE IF NOT EXISTS words_word_fts4 USING fts4(wordid INTEGER not null, word);
INSERT INTO words_word_fts4 SELECT wordid, word from words;
