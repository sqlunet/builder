SELECT 'BNC' AS section;

SELECT 'usage' AS subsection;

SELECT 'most common words' AS comment;
SELECT * FROM 
bnc_bncs
INNER JOIN words USING (wordid)
ORDER BY freq DESC
LIMIT 20;

SELECT 'most common adj' AS comment;
SELECT * FROM 
bnc_bncs
INNER JOIN words USING (wordid)
WHERE posid = 'a'
ORDER BY freq DESC
LIMIT 20;

SELECT 'most common nouns' AS comment;
SELECT * FROM 
bnc_bncs
INNER JOIN words USING (wordid)
WHERE posid = 'n'
ORDER BY freq DESC
LIMIT 20;

SELECT 'most common nouns in speech' AS comment;
SELECT * FROM 
bnc_spwrs
INNER JOIN words USING (wordid)
WHERE posid = 'n'
ORDER BY freq1 DESC
LIMIT 20;

SELECT 'most common nouns in writing' AS comment;
SELECT * FROM 
bnc_spwrs
INNER JOIN words USING (wordid)
WHERE posid = 'n'
ORDER BY freq2 DESC
LIMIT 20;
