DROP TABLE IF EXISTS fn_words_word_fts4;
CREATE VIRTUAL TABLE IF NOT EXISTS fn_words_word_fts4 USING fts4(fnwordid INTEGER not null, word);
INSERT INTO fn_words_word_fts4 SELECT fnwordid, word from fn_words;

