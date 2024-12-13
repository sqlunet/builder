INSERT INTO `relations` (`relationid`,`relation`,`recurses`) VALUES
(500, 'collocation',0);

INSERT INTO `lexrelations` (`synset1id`,`lu1id`,`word1id`,`synset2id`,`lu2id`,`word2id`,`relationid`)
SELECT y.`synset1id`, s1.`luid`, y.`word1id`, y.`synset2id`,s2.`luid`,y.`word2id`, 500
FROM `sn_syntagms` AS y
INNER JOIN senses AS s1 ON y.`word1id` = s1.`wordid` AND y.`synset1id` = s1.`synsetid`
INNER JOIN senses AS s2 ON y.`word2id` = s2.`wordid` AND y.`synset2id` = s2.`synsetid`
;
