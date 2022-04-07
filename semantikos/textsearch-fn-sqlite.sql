DROP TABLE IF EXISTS fn_sentences_text_fts4;
CREATE VIRTUAL TABLE IF NOT EXISTS fn_sentences_text_fts4 USING fts4(sentenceid INTEGER not null, annosetid INTEGER, frameid INTEGER, luid INTEGER, text);
INSERT INTO fn_sentences_text_fts4 SELECT sentenceid, annosetid, f.frameid, luid, text FROM fn_sentences INNER JOIN fn_annosets USING (sentenceid) LEFT JOIN fn_frames AS f USING (frameid) LEFT JOIN fn_lexunits AS l USING (luid) WHERE f.frameid IS NOT NULL;

DROP TABLE IF EXISTS fn_words_word_fts4;
CREATE VIRTUAL TABLE IF NOT EXISTS fn_words_word_fts4 USING fts4(fnwordid INTEGER not null, word);
INSERT INTO fn_words_word_fts4 SELECT fnwordid, word from fn_words;
