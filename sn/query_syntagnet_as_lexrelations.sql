SELECT w1.word, s1.sensekey, w2.word, s2.sensekey
FROM words AS w1
LEFT JOIN `senses` AS s1 USING (`wordid`)
LEFT JOIN `lexrelations` AS r ON (r.`word1id` = s1.`wordid` AND r.`synset1id` = s1.`synsetid`)
LEFT JOIN `senses` AS s2 ON (r.`word2id` = s2.`wordid` AND r.`synset2id` = s2.`synsetid`)
LEFT JOIN `words` AS w2 ON (s2.`wordid` = w2.`wordid`)
WHERE relationid = 500 AND (w1.word = 'abandon' OR w2.word = 'abandon');


SELECT w1.word, s1.sensekey, w2.word, s2.sensekey
FROM words AS w1
LEFT JOIN `senses` AS s1 USING (`wordid`)
LEFT JOIN `lexrelations` AS r ON (r.`word1id` = s1.`wordid` AND r.`synset1id` = s1.`synsetid`)
LEFT JOIN `senses` AS s2 ON (r.`word2id` = s2.`wordid` AND r.`synset2id` = s2.`synsetid`)
LEFT JOIN `words` AS w2 ON (s2.`wordid` = w2.`wordid`)
WHERE relationid = 500 AND (w1.word = 'king' OR w2.word = 'king');
