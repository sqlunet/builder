SELECT 'SyntagNet' AS section;

SELECT 'collocations' AS subsection;

SELECT 'collocations of "fire"' AS comment;
SELECT 
	word1id, 
	w1.word AS lemma1,
    synset1id AS synset1id,
    s1.posid AS pos1,
    s1.definition AS definition1,
    word2id,
    w2.word AS lemma2,
    synset2id,
    s2.posid AS pos2,
    s2.definition AS definition2,
    w2.word = 'fire' AS second
FROM sn_syntagms
JOIN words AS w1 ON (word1id = w1.wordid) 
JOIN words AS w2 ON (word2id = w2.wordid)
JOIN synsets AS s1 ON (synset1id = s1.synsetid)
JOIN synsets AS s2 ON (synset2id = s2.synsetid)
WHERE w1.word = 'fire' OR w2.word = 'fire'
ORDER BY second,w1.word, w2.word;

