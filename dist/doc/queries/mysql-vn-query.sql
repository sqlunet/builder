SELECT 'VerbNet' AS section;

CREATE OR REPLACE VIEW vn_examplesets AS
SELECT frameid,GROUP_CONCAT(example ORDER BY exampleid ASC separator '|') AS exampleset 
FROM vn_frames_examples INNER JOIN vn_examples USING (exampleid) GROUP BY frameid;

SELECT 'classes' AS subsection;

SELECT 'members of vn_classes' AS comment;
SELECT class,COUNT(vnwordid) AS N,SUBSTRING(GROUP_CONCAT(word) FROM 1 FOR 80)
FROM vn_members
INNER JOIN vn_classes USING (classid)
INNER JOIN vn_words USING (vnwordid)
GROUP BY classid
ORDER BY N DESC
LIMIT 20;

SELECT 'vn_classes of verb' AS comment;
SELECT word,COUNT(classid) N,SUBSTRING(GROUP_CONCAT(class) FROM 1 FOR 80)
FROM vn_members
INNER JOIN vn_classes USING (classid)
INNER JOIN vn_words USING (vnwordid)
GROUP BY vnwordid
ORDER BY N DESC
LIMIT 20;

SELECT 'vn class members' AS comment;
SELECT *
FROM vn_members
LEFT JOIN vn_words USING (vnwordid)
ORDER BY classid, word
LIMIT 20;

SELECT 'vn class member senses' AS comment;
SELECT *
FROM vn_members_senses
LEFT JOIN vn_words USING (vnwordid)
ORDER BY classid, word
LIMIT 20;

SELECT 'vn class "accompany-51.27", selected by id' AS comment;
SELECT *
FROM vn_classes
INNER JOIN vn_members_senses USING (classid)
LEFT JOIN vn_words USING (vnwordid)
WHERE classid = 2
ORDER BY word;

SELECT 'vn class "accompany-51.27", selected by name' AS comment;
SELECT *
FROM vn_classes
INNER JOIN vn_members_senses USING (classid)
LEFT JOIN vn_words USING (vnwordid)
WHERE class LIKE 'accompany%'
ORDER BY word;

SELECT 'vn class, word, senses' AS comment;
SELECT class,word, GROUP_CONCAT(sensekey)
FROM vn_members_senses
LEFT JOIN vn_words USING (vnwordid,wordid)
LEFT JOIN vn_classes USING (classid)
GROUP BY wordid,word,classid,class
ORDER BY classid, word
LIMIT 20;

SELECT 'roletypes' AS subsection;

SELECT 'vn role types' AS comment;
SELECT * 
FROM vn_roletypes;

SELECT 'roles' AS subsection;

SELECT 'get verbnet thematic roles for verb "want"' AS comment;
SELECT v.word,SUBSTRING(definition FROM 1 FOR 32),class,roletype,restrs
FROM words AS w
LEFT JOIN senses USING (wordid) 
LEFT JOIN synsets USING (synsetid)
LEFT JOIN vn_words AS v USING (wordid) 
INNER JOIN vn_members USING (vnwordid)
INNER JOIN vn_roles USING (classid) 
INNER JOIN vn_roletypes USING (roletypeid) 
LEFT JOIN vn_restrs USING (restrsid) 
INNER JOIN vn_classes USING (classid)
WHERE posid='v' AND w.word='want'
ORDER BY synsetid,classid,roletype;

SELECT 'vn class (and word members) having "Stimulus" role' AS comment;
SELECT class,GROUP_CONCAT(w.word)
FROM words AS w
LEFT JOIN senses USING (wordid) 
LEFT JOIN synsets USING (synsetid)
LEFT JOIN vn_words AS v USING (wordid) 
INNER JOIN vn_members USING (vnwordid)
INNER JOIN vn_roles USING (classid) 
INNER JOIN vn_roletypes USING (roletypeid) 
LEFT JOIN vn_restrs USING (restrsid) 
INNER JOIN vn_classes USING (classid)
WHERE roletype = 'Stimulus'
GROUP BY class,roleid;

SELECT 'vn roles for "guide" by synset' AS comment;
SELECT synsetid,class,w.word,GROUP_CONCAT(roletype ORDER BY roletype),SUBSTRING(definition FROM 1 FOR 64)
FROM words AS w
LEFT JOIN senses USING (wordid) 
LEFT JOIN synsets USING (synsetid)
LEFT JOIN vn_words AS v USING (wordid) 
INNER JOIN vn_members USING (vnwordid)
INNER JOIN vn_roles USING (classid) 
INNER JOIN vn_roletypes USING (roletypeid) 
LEFT JOIN vn_restrs USING (restrsid) 
INNER JOIN vn_classes USING (classid)
WHERE w.word = 'guide'
GROUP BY synsetid,classid
ORDER BY synsetid,classid;

SELECT 'vn roles for "guide" by synset (VerbNet-like entry)' AS comment;
SELECT w.word,class,sensekey,roletype,restrs
FROM words AS w
LEFT JOIN senses USING (wordid) 
LEFT JOIN synsets USING (synsetid) 
LEFT JOIN vn_words AS v USING (wordid) 
INNER JOIN vn_members USING (vnwordid)
INNER JOIN vn_roles USING (classid) 
INNER JOIN vn_roletypes USING (roletypeid) 
LEFT JOIN vn_restrs USING (restrsid) 
INNER JOIN vn_classes USING (classid)
WHERE w.word = 'guide'
ORDER BY classid,sensekey,roletype,restrs;

SELECT 'frames' AS subsection;

SELECT 'get verbnet frame (syntax/semantics/example) for verb "want"' AS comment;
SELECT w.word,SUBSTRING(definition FROM 1 FOR 32),framename,framesubname,syntax,semantics,example
FROM words AS w
LEFT JOIN senses USING (wordid) 
LEFT JOIN synsets USING (synsetid) 
LEFT JOIN vn_words AS v USING (wordid) 
INNER JOIN vn_members USING (vnwordid)
INNER JOIN vn_classes_frames USING (classid) 
INNER JOIN vn_frames USING (frameid) 
INNER JOIN vn_syntaxes USING (syntaxid) 
INNER JOIN vn_semantics USING (semanticsid) 
INNER JOIN vn_framenames USING (framenameid) 
LEFT JOIN vn_framesubnames USING (framesubnameid) 
LEFT JOIN vn_frames_examples USING (frameid) 
LEFT JOIN vn_examples USING (exampleid) 
INNER JOIN vn_classes USING (classid)
WHERE posid='v' AND w.word='want';

SELECT 'word whose frames have semantics involving the "rotational_motion" predicate' AS comment;
SELECT w.word,SUBSTRING(definition FROM 1 FOR 32),framename,framesubname,frameid,SUBSTRING(semantics FROM 1 FOR 32)
FROM words AS w
LEFT JOIN senses USING (wordid) 
LEFT JOIN synsets USING (synsetid) 
LEFT JOIN vn_words AS v USING (wordid) 
INNER JOIN vn_members USING (vnwordid)
INNER JOIN vn_classes_frames USING (classid) 
INNER JOIN vn_frames USING (frameid) 
INNER JOIN vn_syntaxes USING (syntaxid) 
INNER JOIN vn_semantics USING (semanticsid) 
INNER JOIN vn_framenames USING (framenameid) 
LEFT JOIN vn_framesubnames USING (framesubnameid) 
LEFT JOIN vn_frames_examples USING (frameid) 
LEFT JOIN vn_examples USING (exampleid) 
INNER JOIN vn_classes USING (classid)
INNER JOIN vn_predicates_semantics USING (semanticsid)
INNER JOIN vn_predicates USING (predicateid)
WHERE predicate='rotational_motion'
ORDER BY word,framename,framesubname;

SELECT 'groupings' AS subsection;

SELECT 'vn groupings' AS comment;
SELECT * 
FROM vn_members_groupings 
LEFT JOIN vn_groupings USING(groupingid)
LEFT JOIN vn_words USING(vnwordid) 
LEFT JOIN vn_classes USING (classid) 
ORDER BY groupingid
LIMIT 20;

SELECT 'vn groupings: grouping, class, word' AS comment;
SELECT groupingid,`grouping`, class, word 
FROM vn_members_groupings 
LEFT JOIN vn_groupings USING(groupingid)  
LEFT JOIN vn_words USING(vnwordid) 
LEFT JOIN vn_classes USING (classid) 
ORDER BY groupingid
LIMIT 20;

SELECT 'vn groupings: grouping, classes, word' AS comment;
SELECT word,`grouping`,GROUP_CONCAT(class) 
FROM vn_members_groupings LEFT JOIN vn_groupings USING(groupingid)  
LEFT JOIN vn_words USING(vnwordid) 
LEFT JOIN vn_classes USING (classid) 
GROUP BY word,`grouping`,groupingid
ORDER BY groupingid
LIMIT 20;

SELECT 'wordnet' AS subsection;

SELECT 'vn roles/frames for "guide%2:38" (VerbNet-like entry)' AS comment;
SELECT sensekey,class,framename,framesubname,number,xtag,syntax,semantics,exampleset,roletype,restrs
FROM words AS w
LEFT JOIN senses USING (wordid) 
LEFT JOIN synsets USING (synsetid) 
LEFT JOIN vn_words AS v USING (wordid) 
INNER JOIN vn_members USING (vnwordid)
INNER JOIN vn_classes_frames USING (classid) 
INNER JOIN vn_roles USING (classid) 
INNER JOIN vn_roletypes USING (roletypeid) 
LEFT JOIN vn_restrs USING (restrsid) 
INNER JOIN vn_frames USING (frameid) 
INNER JOIN vn_syntaxes USING (syntaxid) 
INNER JOIN vn_semantics USING (semanticsid) 
INNER JOIN vn_framenames USING (framenameid) 
LEFT JOIN vn_framesubnames USING (framesubnameid) 
LEFT JOIN vn_frames_examples USING (frameid) 
LEFT JOIN vn_examples USING (exampleid) 
INNER JOIN vn_classes USING (classid) 
LEFT JOIN vn_examplesets USING (frameid)
WHERE sensekey LIKE 'guide%%2:38%'
GROUP BY sensekey,synsetid,class,frameid,framename,framesubname,number,xtag,syntax,semantics,exampleset,roletypeid,roletype,restrs
ORDER BY synsetid,class ASC,frameid ASC,roletypeid ASC;

SELECT 'statistics' AS subsection;

SELECT 'stats: frame maps' AS comment;
SELECT COUNT(*)
FROM vn_classes_frames;
