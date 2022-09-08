SELECT 'FrameNet (semtypes)' AS section;

SELECT 'semtypes' AS subsection;

SELECT 'semtype''s supertype' AS comment;
SELECT t.semtype, s.semtype AS supersemtype
FROM fn_semtypes_supers
LEFT JOIN fn_semtypes AS t USING (semtypeid)
LEFT JOIN fn_semtypes AS s ON (supersemtypeid = s.semtypeid)
ORDER BY t.semtype
LIMIT 20;

SELECT 'semtype''s subtypes' AS comment;
SELECT s.semtype, GROUP_CONCAT(t.semtype) AS subsemtypes
FROM fn_semtypes_supers
LEFT JOIN fn_semtypes AS t USING (semtypeid)
LEFT JOIN fn_semtypes AS s ON (supersemtypeid = s.semtypeid)
GROUP BY s.semtype
ORDER BY s.semtype
LIMIT 20;

SELECT 'frame semtypes distribution' AS comment;
SELECT semtype, COUNT(*)
FROM fn_frames_semtypes
LEFT JOIN fn_semtypes USING (semtypeid)
GROUP BY semtypeid
ORDER BY semtype;

SELECT 'frame semtypes' AS comment;
SELECT frame,GROUP_CONCAT(semtype)
FROM fn_frames_semtypes
LEFT JOIN fn_frames USING (frameid)
LEFT JOIN fn_semtypes USING (semtypeid)
WHERE semtypeid IS NOT NULL
GROUP BY frame
ORDER BY frame
LIMIT 32,20;

SELECT 'fe semtypes distribution' AS comment;
SELECT semtype, COUNT(*)
FROM fn_fes_semtypes
LEFT JOIN fn_semtypes USING (semtypeid)
GROUP BY semtypeid
ORDER BY semtype;

SELECT 'frame elements for "Causation"' AS comment;
SELECT frame,fetype,semtype
FROM fn_frames 
LEFT JOIN fn_fes USING (frameid) 
LEFT JOIN fn_fetypes USING (fetypeid)
LEFT JOIN fn_fes_semtypes USING (feid) 
LEFT JOIN fn_semtypes USING (semtypeid) 
WHERE frame='Causation'
ORDER BY feid;

SELECT 'frame elements with multiple semtypes' AS comment;
SELECT frame,fetype,GROUP_CONCAT(semtype)
FROM fn_fes
LEFT JOIN fn_frames USING(frameid)
LEFT JOIN fn_fetypes USING(fetypeid)
LEFT JOIN fn_fes_semtypes USING(feid)
LEFT JOIN fn_semtypes USING (semtypeid)
WHERE feid IN (SELECT feid FROM fn_fes_semtypes GROUP BY feid HAVING COUNT(*) > 1)
GROUP BY frameid,feid;

SELECT 'frame elements of "Sentient" semtype' AS comment;
SELECT frame,fetype,semtype
FROM fn_fes
LEFT JOIN fn_frames USING(frameid)
LEFT JOIN fn_fetypes USING(fetypeid)
LEFT JOIN fn_fes_semtypes USING(feid)
LEFT JOIN fn_semtypes USING (semtypeid)
WHERE semtype = 'Sentient'
LIMIT 20;

SELECT 'frame elements of "Sentient" semtype' AS comment;
SELECT frame,fetype,semtype
FROM fn_fes
LEFT JOIN fn_frames USING(frameid)
LEFT JOIN fn_fetypes USING(fetypeid)
LEFT JOIN fn_fes_semtypes USING(feid)
LEFT JOIN fn_semtypes USING (semtypeid)
WHERE semtype = 'Human'
LIMIT 20;

