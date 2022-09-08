SELECT 'FrameNet (lexunits)' AS section;

SELECT 'lexunits' AS subsection;

SELECT 'lexunits' AS comment;
SELECT frame,lexunit,ludefinition
FROM fn_lexunits
LEFT JOIN fn_frames USING (frameid)
ORDER BY frame,lexunit
LIMIT 50;

SELECT 'lexunits grouped by frame' AS comment;
SELECT frame,GROUP_CONCAT(lexunit)
FROM fn_lexunits
LEFT JOIN fn_frames USING (frameid)
GROUP BY frame
ORDER BY frame
LIMIT 50;

SELECT 'lexunits for frame' AS comment;
SELECT frame,lexunit,ludefinition
FROM fn_lexunits
LEFT JOIN fn_frames USING (frameid)
WHERE frame = 'Activity_pause'
ORDER BY lexunit;

SELECT 'frame for lexunit' AS comment;
SELECT lexunit,ludefinition,frame
FROM fn_lexunits
LEFT JOIN fn_frames USING (frameid)
WHERE lexunit LIKE 'freeze.%'
ORDER BY frame;

SELECT 'multiple lexunit' AS comment;
SELECT * FROM fn_lexemes 
LEFT JOIN fn_words USING (fnwordid) 
LEFT JOIN fn_lexunits USING (luid) 
WHERE lexunit LIKE 'target.n';

SELECT 'lexemes' AS subsection;

SELECT 'lexunit like "bring*"' AS comment;
SELECT lexunit,ludefinition,ludict
FROM fn_lexunits
WHERE lexunit LIKE 'bring%'
ORDER BY lexunit;

SELECT 'lexunit "bring to mind.v"' AS comment;
SELECT lexunit,p.posid,lexemeidx,word,pl.posid,headword,breakbefore
FROM fn_lexunits AS lu
LEFT JOIN fn_lexemes AS l USING (luid)
LEFT JOIN fn_words USING (fnwordid)
LEFT JOIN fn_poses AS p ON (lu.posid = p.posid)
LEFT JOIN fn_poses AS pl ON (l.posid = pl.posid)
WHERE lexunit = 'bring to mind.v'
ORDER BY lexemeidx;

SELECT 'lexunits containing lexeme "bring"' AS comment;
SELECT frame,lexunit,ludefinition,p.posid,lexemeidx,word,pl.posid,headword,breakbefore
FROM fn_lexunits AS lu
LEFT JOIN fn_lexemes AS l USING (luid)
LEFT JOIN fn_words USING (fnwordid)
LEFT JOIN fn_poses AS p ON (lu.posid = p.posid)
LEFT JOIN fn_poses AS pl ON (l.posid = pl.posid)
LEFT JOIN fn_frames USING (frameid)
WHERE word = 'bring'
ORDER BY lexemeidx;

SELECT 'lexemes that make up a lexunit' AS comment;
SELECT word 
FROM fn_lexemes 
LEFT JOIN fn_words USING (fnwordid) 
LEFT JOIN fn_lexunits USING (luid) 
WHERE lexemeidx IS NULL
LIMIT 20;
SELECT COUNT(*) 
FROM fn_lexemes 
WHERE lexemeidx IS NULL;

SELECT 'lexemes that are contained in multi-lexeme lexunits' AS comment;
SELECT word 
FROM fn_lexemes 
LEFT JOIN fn_words USING (fnwordid) 
LEFT JOIN fn_lexunits USING (luid) 
WHERE lexemeidx IS NOT NULL
LIMIT 20;
SELECT COUNT(*) FROM fn_lexemes WHERE lexemeidx IS NOT NULL;

SELECT 'break lexunit in lexemes' AS comment;
SELECT * 
FROM fn_lexemes 
LEFT JOIN fn_words USING (fnwordid) 
LEFT JOIN fn_lexunits USING (luid) 
WHERE lexunit LIKE 'get_back%';
SELECT * 
FROM fn_lexemes 
LEFT JOIN fn_words USING (fnwordid) 
LEFT JOIN fn_lexunits USING (luid) 
WHERE lexunit LIKE 'bring to mind%';

SELECT 'lexemes per positionid index' AS comment;
SELECT lexemeidx, COUNT(*) 
FROM fn_lexemes 
GROUP BY lexemeidx;

SELECT 'breakbefore lexemes' AS comment;
SELECT word,lexunit 
FROM fn_lexemes 
LEFT JOIN fn_words USING (fnwordid) 
LEFT JOIN fn_lexunits USING (luid) 
WHERE breakbefore LIMIT 20; 

SELECT 'breakbefore lexeme count' AS comment;
SELECT breakbefore, COUNT(*) 
FROM fn_lexemes 
GROUP BY breakbefore;

SELECT 'headword count' AS comment;
SELECT headword, COUNT(*) 
FROM fn_lexemes 
GROUP BY headword;

SELECT 'lexunit (with frames) containing word' AS comment;
SELECT lexunit,ludefinition,ludict,p.posid,pl.posid,breakbefore,headword,frame
FROM fn_words
LEFT JOIN fn_lexemes AS l USING (fnwordid) 
LEFT JOIN fn_lexunits AS lu USING (luid) 
LEFT JOIN fn_poses AS p ON (lu.posid = p.posid) 
LEFT JOIN fn_poses AS pl ON (l.posid = pl.posid) 
LEFT JOIN fn_frames USING (frameid) 
WHERE word = 'bring';

SELECT 'governors' AS subsection;

SELECT 'governor types' AS comment;
SELECT DISTINCT governortype FROM fn_governors;

SELECT 'governor for lexunit' AS comment;
SELECT word AS governor,lexunit,governortype 
FROM fn_governors 
LEFT JOIN fn_lexunits_governors USING (governorid) 
LEFT JOIN fn_lexunits USING (luid) 
LEFT JOIN fn_words USING (fnwordid) 
ORDER BY lexunit,governor
LIMIT 20;

SELECT 'governors for lexunit' AS comment;
SELECT word AS governor,lexunit 
FROM fn_governors 
LEFT JOIN fn_lexunits_governors USING (governorid) 
LEFT JOIN fn_lexunits USING (luid) 
LEFT JOIN fn_words USING (fnwordid) 
WHERE lexunit = 'gossip.n' 
ORDER BY word;

SELECT 'governor governing multiple lexical units' AS comment;
SELECT word AS governor,GROUP_CONCAT(lexunit) 
FROM fn_governors 
LEFT JOIN fn_lexunits_governors USING (governorid) 
LEFT JOIN fn_lexunits USING (luid) 
LEFT JOIN fn_words USING (fnwordid) 
GROUP BY wordid,word HAVING COUNT(*) > 1
ORDER BY governor
LIMIT 20;

SELECT 'multiply governed lexical units' AS comment;
SELECT GROUP_CONCAT(word) AS governors, lexunit 
FROM fn_governors 
LEFT JOIN fn_lexunits_governors USING (governorid) 
LEFT JOIN fn_lexunits USING (luid) 
LEFT JOIN fn_words USING (fnwordid) 
GROUP BY luid HAVING COUNT(*) > 1
ORDER BY lexunit
LIMIT 20;

SELECT 'examples' AS subsection;

SELECT 'governors for lexunit "gossip.n" with examples' AS comment;
SELECT word AS governor,lexunit,`text` 
FROM fn_governors 
LEFT JOIN fn_governors_annosets USING (governorid) 
LEFT JOIN fn_annosets USING (annosetid) 
LEFT JOIN fn_sentences USING (sentenceid) 
LEFT JOIN fn_lexunits_governors AS fg USING (governorid) 
LEFT JOIN fn_lexunits AS lu ON (fg.luid = lu.luid) 
LEFT JOIN fn_words USING (fnwordid) 
WHERE lexunit = 'gossip.n' 
ORDER BY word;

SELECT 'governors for lexunit "acclaim.n" with examples' AS comment;
SELECT word AS governor,lexunit,`text` 
FROM fn_governors 
LEFT JOIN fn_governors_annosets USING (governorid) 
LEFT JOIN fn_annosets USING (annosetid) 
LEFT JOIN fn_sentences USING (sentenceid) 
LEFT JOIN fn_lexunits_governors AS fg USING (governorid) 
LEFT JOIN fn_lexunits AS lu ON (fg.luid = lu.luid) 
LEFT JOIN fn_words USING (fnwordid) 
WHERE lexunit = 'acclaim.n' 
ORDER BY word;
