SELECT 'PropBank' AS section;

SELECT 'propbank' AS subsection;

SELECT 'rolesets per word' AS comment;
SELECT word,COUNT(*) AS c 
FROM pb_rolesets 
LEFT JOIN pb_words USING (pbwordid) 
GROUP BY pbwordid HAVING c > 1 
ORDER BY c DESC
LIMIT 20;

SELECT 'rolesetheads matching several lemmas' AS comment;
SELECT rolesethead, GROUP_CONCAT(DISTINCT word),COUNT(*) AS c 
FROM pb_rolesets 
LEFT JOIN pb_words USING (pbwordid) 
GROUP BY rolesethead HAVING c > 1 
ORDER BY c DESC
LIMIT 20;

SELECT 'rolesets headed by "go"' AS comment;
SELECT * 
FROM pb_rolesets 
LEFT JOIN pb_words USING (pbwordid) 
WHERE rolesethead = 'go';

SELECT 'word''s roleset' AS comment;
SELECT * 
FROM pb_words
LEFT JOIN pb_rolesets USING (pbwordid)
WHERE word = 'accompany';

SELECT 'word''s rolesets' AS comment;
SELECT * 
FROM pb_words
LEFT JOIN pb_rolesets USING (pbwordid)
WHERE word = 'admit';

SELECT 'word having multiple rolesets' AS comment;
SELECT word,GROUP_CONCAT(CONCAT(rolesetname,' (',rolesetdescr,')'))
FROM pb_words
LEFT JOIN pb_rolesets USING (pbwordid)
GROUP BY word HAVING COUNT(*) > 1
ORDER BY word
LIMIT 20;

SELECT 'word having most rolesets' AS comment;
SELECT word, COUNT(*) AS c,GROUP_CONCAT(CONCAT(rolesetname,' (',rolesetdescr,')'))
FROM pb_words
LEFT JOIN pb_rolesets USING (pbwordid)
GROUP BY pbwordid
ORDER BY c DESC
LIMIT 20;

SELECT 'word''s roles' AS comment;
SELECT word,rolesetid,rolesetname,rolesetdescr,argtypeid,roledescr,theta
FROM pb_words
LEFT JOIN pb_rolesets USING (pbwordid)
LEFT JOIN pb_roles USING (rolesetid)
LEFT JOIN pb_thetas USING (thetaid)
WHERE word = 'abandon'
ORDER BY rolesetid,argtypeid;

SELECT 'word''s roles by roleset' AS comment;
SELECT word,rolesetid,rolesetname,rolesetdescr,GROUP_CONCAT(CONCAT(argtypeid,'-',roledescr,'-',IFNULL(theta, '?')) ORDER BY argtypeid SEPARATOR ' / ')
FROM pb_words
LEFT JOIN pb_rolesets USING (pbwordid)
LEFT JOIN pb_roles USING (rolesetid)
LEFT JOIN pb_thetas USING (thetaid)
WHERE word = 'abandon'
GROUP BY rolesetid
ORDER BY rolesetid;

SELECT 'word''s examples' AS comment;
SELECT word,rolesetname,rolesetdescr,exampleid,`text`,form,aspect,tense,person,voice
FROM pb_words
LEFT JOIN pb_rolesets USING (pbwordid)
LEFT JOIN pb_examples USING (rolesetid)
LEFT JOIN pb_aspects USING (aspectid)
LEFT JOIN pb_forms USING (formid)
LEFT JOIN pb_tenses USING (tenseid)
LEFT JOIN pb_voices USING (voiceid)
LEFT JOIN pb_persons USING (personid)
WHERE word = 'abandon'
ORDER BY rolesetid, exampleid;

SELECT 'examples' AS comment;
SELECT word,rolesetname,rolesetdescr,exampleid,`text`,form,aspect,tense,person,voice
FROM pb_words
LEFT JOIN pb_rolesets USING (pbwordid)
LEFT JOIN pb_examples USING (rolesetid)
LEFT JOIN pb_aspects USING (aspectid)
LEFT JOIN pb_forms USING (formid)
LEFT JOIN pb_tenses USING (tenseid)
LEFT JOIN pb_voices USING (voiceid)
LEFT JOIN pb_persons USING (personid)
WHERE exampleid IS NOT NULL AND word LIKE 'ab%'
ORDER BY rolesetid, exampleid
LIMIT 20;

SELECT 'examples with passive' AS comment;
SELECT word,rolesetname,rolesetdescr,exampleid,`text`,form,aspect,tense,person,voice
FROM pb_words
LEFT JOIN pb_rolesets USING (pbwordid)
LEFT JOIN pb_examples USING (rolesetid)
LEFT JOIN pb_aspects USING (aspectid)
LEFT JOIN pb_forms USING (formid)
LEFT JOIN pb_tenses USING (tenseid)
LEFT JOIN pb_voices USING (voiceid)
LEFT JOIN pb_persons USING (personid)
WHERE voice = 'passive'
ORDER BY rolesetid, exampleid
LIMIT 50,70;

SELECT 'examples with infinitive' AS comment;
SELECT word,rolesetname,rolesetdescr,exampleid,`text`,form,aspect,tense,person,voice
FROM pb_words
LEFT JOIN pb_rolesets USING (pbwordid)
LEFT JOIN pb_examples USING (rolesetid)
LEFT JOIN pb_aspects USING (aspectid)
LEFT JOIN pb_forms USING (formid)
LEFT JOIN pb_tenses USING (tenseid)
LEFT JOIN pb_voices USING (voiceid)
LEFT JOIN pb_persons USING (personid)
WHERE form = 'infinitive'
ORDER BY rolesetid, exampleid
LIMIT 20;

SELECT 'word''s examples broken into arguments' AS comment;
SELECT word,rolesetname,rolesetdescr,exampleid,e.`text`,rel,arg,argtypeid,func
FROM pb_words
LEFT JOIN pb_rolesets AS s USING (pbwordid)
LEFT JOIN pb_examples AS e USING (rolesetid)
LEFT JOIN pb_rels AS r USING (exampleid)
LEFT JOIN pb_args AS a USING (exampleid)
LEFT JOIN pb_funcs AS f ON (f.funcid = a.funcid)
WHERE word IN ('abandon','admit','accept')
ORDER BY rolesetid, e.exampleid, a.argtypeid;

SELECT 'word''s examples broken down into logical formid' AS comment;
SELECT word,rolesetname,rolesetdescr,exampleid,e.`text`,CONCAT(rel,'(',GROUP_CONCAT(CONCAT('[',a.argtypeid,']{',r.roledescr,'}->',arg) ORDER BY a.argtypeid),')') AS analysis
FROM pb_words
LEFT JOIN pb_rolesets AS s USING (pbwordid)
LEFT JOIN pb_examples AS e USING (rolesetid)
LEFT JOIN pb_rels AS f USING (exampleid)
LEFT JOIN pb_args AS a USING (exampleid)
LEFT JOIN pb_roles AS r ON (s.rolesetid = r.rolesetid AND r.argtypeid = a.argtypeid)
WHERE word IN ('abandon','admit','accept')
GROUP BY exampleid,e.`text`,word,s.rolesetid,rolesetname,rolesetdescr,e.`text`,rel
ORDER BY s.rolesetid, e.exampleid;

SELECT 'funcid' AS comment;
SELECT * FROM pb_funcs;

SELECT 'funcid distribution' AS comment;
SELECT funcid, func, funcdescr, COUNT(*) AS c 
FROM pb_roles 
LEFT JOIN pb_funcs USING (funcid) 
GROUP BY funcid 
ORDER BY c DESC;

SELECT 'funcid having 1 occurrence' AS comment;
SELECT func,GROUP_CONCAT(argid) 
FROM pb_args 
LEFT JOIN pb_funcs USING (funcid) 
GROUP BY funcid HAVING COUNT(*)=1;

SELECT 'location of funcid having 1 occurrence' AS comment;
SELECT func,GROUP_CONCAT(argid),GROUP_CONCAT(CONCAT(rolesethead,'.xml'))
FROM pb_args 
LEFT JOIN pb_funcs USING (funcid) 
LEFT JOIN pb_examples USING (exampleid)
LEFT JOIN pb_rolesets USING (rolesetid)
GROUP BY funcid HAVING COUNT(*)=1;

SELECT 'distribution of argtypeid in roles' AS comment;
SELECT argtypeid, argtype, COUNT(*) AS c 
FROM pb_roles 
LEFT JOIN pb_argtypes USING (argtypeid)
GROUP BY argtypeid 
ORDER BY c DESC;

SELECT 'distribution of argtypeid in args' AS comment;
SELECT argtypeid, argtype, COUNT(*) AS c 
FROM pb_args 
LEFT JOIN pb_argtypes USING (argtypeid) 
GROUP BY argtypeid ORDER BY c DESC;

SELECT 'verbnet through semlink' AS subsection;

SELECT 'propbank-verbnet through semlink' AS comment;
SELECT rolesetname,argtypeid,theta,classtag,roletype
FROM pb_pbroles_vnroles AS m
LEFT JOIN pb_rolesets USING (rolesetid)
LEFT JOIN pb_roles USING (roleid)
LEFT JOIN pb_thetas USING (thetaid)
LEFT JOIN vn_classes AS c ON (m.vnclassid = c.classid)
LEFT JOIN vn_roles AS r ON (m.vnroleid = r.roleid)
LEFT JOIN vn_roletypes AS t ON (r.roletypeid = t.roletypeid)
ORDER BY m.rolesetid, argtypeid
LIMIT 20;

SELECT 'propbank-verbnet through semlink, where roles differ' AS comment;
SELECT rolesetname,argtypeid,theta,classtag,roletype
FROM pb_pbroles_vnroles AS m
LEFT JOIN pb_rolesets USING (rolesetid)
LEFT JOIN pb_roles USING (roleid)
LEFT JOIN pb_thetas USING (thetaid)
LEFT JOIN vn_classes AS c ON (m.vnclassid = c.classid)
LEFT JOIN vn_roles AS r ON (m.vnroleid = r.roleid)
LEFT JOIN vn_roletypes AS t ON (r.roletypeid = t.roletypeid)
WHERE roletype <> theta
ORDER BY m.rolesetid, argtypeid
LIMIT 20;

SELECT 'propbank-verbnet through semlink, how roles differ' AS comment;
SELECT COUNT(*)
FROM pb_pbroles_vnroles AS m
LEFT JOIN pb_rolesets USING (rolesetid)
LEFT JOIN pb_roles USING (roleid)
LEFT JOIN pb_thetas USING (thetaid)
LEFT JOIN vn_classes AS c ON (m.vnclassid = c.classid)
LEFT JOIN vn_roles AS r ON (m.vnroleid = r.roleid)
LEFT JOIN vn_roletypes AS t ON (r.roletypeid = t.roletypeid)
WHERE roletype <> theta
ORDER BY m.rolesetid, argtypeid;
SELECT COUNT(*) FROM pb_pbroles_vnroles;
