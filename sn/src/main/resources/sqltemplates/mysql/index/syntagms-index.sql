ALTER TABLE ${syntagms.table} ADD INDEX k_${syntagms.table}_${syntagms.synset1id}_${syntagms.word1id} (${syntagms.synset1id},${syntagms.word1id});
ALTER TABLE ${syntagms.table} ADD INDEX k_${syntagms.table}_${syntagms.synset2id}_${syntagms.word2id} (${syntagms.synset2id},${syntagms.word2id});
