CREATE TABLE IF NOT EXISTS ${subcorpuses_sentences.table} (
${subcorpuses_sentences.subcorpusid} INTEGER NOT NULL,
${subcorpuses_sentences.sentenceid} INTEGER NOT NULL,
PRIMARY KEY (${subcorpuses_sentences.subcorpusid},${subcorpuses_sentences.sentenceid}) );
