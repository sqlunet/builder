DROP VIEW IF EXISTS samplesets;
CREATE VIEW IF NOT EXISTS samplesets AS 
	SELECT synsetid,GROUP_CONCAT(DISTINCT samples.sample) AS sampleset 
	FROM samples GROUP BY synsetid;

DROP VIEW IF EXISTS dict;
CREATE VIEW IF NOT EXISTS dict AS 
	SELECT * FROM words 
	LEFT JOIN senses s USING (wordid) 
	LEFT JOIN casedwords USING (wordid,casedwordid) 
	LEFT JOIN synsets USING (synsetid) 
	LEFT JOIN samplesets USING (synsetid);

