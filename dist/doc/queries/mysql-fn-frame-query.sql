SELECT 'FrameNet (frames)' AS section;

SELECT 'frames' AS subsection;

SELECT 'frames' AS comment;
SELECT frame,framedefinition FROM fn_frames
LIMIT 20;

SELECT 'frame relations' AS subsection;

SELECT 'related frames' AS comment;
SELECT s.frame,GROUP_CONCAT(relation),GROUP_CONCAT(d.frame)
FROM fn_frames AS s
LEFT JOIN fn_frames_related USING (frameid)
LEFT JOIN fn_frames AS d ON (frame2id = d.frameid)
LEFT JOIN fn_framerelations USING (relationid)
GROUP BY s.frame
ORDER BY s.frame
LIMIT 10;

SELECT 'frame "Causation", related frames' AS comment;
SELECT s.frame,relation,d.frame
FROM fn_frames AS s
LEFT JOIN fn_frames_related USING (frameid)
LEFT JOIN fn_frames AS d ON (frame2id = d.frameid)
LEFT JOIN fn_framerelations USING (relationid)
WHERE s.frame='Causation';

SELECT 'frame "Causation", related frames (reverse)' AS comment;
SELECT s.frame,relation,d.frame
FROM fn_frames AS s
LEFT JOIN fn_frames_related USING (frameid)
LEFT JOIN fn_frames AS d ON (frame2id = d.frameid)
LEFT JOIN fn_framerelations USING (relationid)
WHERE d.frame='Causation';

SELECT 'frame "Activity", related frames' AS comment;
SELECT s.frame,relation,d.frame
FROM fn_frames AS s
LEFT JOIN fn_frames_related USING (frameid)
LEFT JOIN fn_frames AS d ON (frame2id = d.frameid)
LEFT JOIN fn_framerelations USING (relationid)
WHERE s.frame='Activity';

SELECT 'frame "Activity", related frames (reverse)' AS comment;
SELECT s.frame,relation,d.frame
FROM fn_frames AS s
LEFT JOIN fn_frames_related USING (frameid)
LEFT JOIN fn_frames AS d ON (frame2id = d.frameid)
LEFT JOIN fn_framerelations USING (relationid)
WHERE d.frame='Activity';

SELECT 'frame elements' AS subsection;

SELECT 'frame elements (fe)' AS comment;
SELECT frame,fetype
FROM fn_fes 
LEFT JOIN fn_frames USING (frameid) 
LEFT JOIN fn_fetypes USING (fetypeid)
ORDER BY frameid,feid
LIMIT 20;

SELECT 'frame elements (fe) grouped by frame' AS comment;
SELECT frame,GROUP_CONCAT(fetype) AS fe
FROM fn_fes 
LEFT JOIN fn_frames USING (frameid) 
LEFT JOIN fn_fetypes USING (fetypeid)
GROUP BY frameid
LIMIT 20;

SELECT 'fe for "Causation" frame' AS comment;
SELECT frame,fetype,coretype,coreset
FROM fn_frames 
LEFT JOIN fn_fes USING (frameid) 
LEFT JOIN fn_fetypes USING (fetypeid)
LEFT JOIN fn_coretypes USING (coretypeid) 
WHERE frame='Causation'
ORDER BY feid;

SELECT 'frame core elements' AS comment;
SELECT frame,GROUP_CONCAT(fetype) AS corefe
FROM fn_fes 
LEFT JOIN fn_frames USING (frameid) 
LEFT JOIN fn_fetypes USING (fetypeid)
WHERE coreset IS NOT NULL
GROUP BY frameid
LIMIT 20;

SELECT 'core fe sets' AS subsection;

SELECT 'frame "Causation", coresets' AS comment;
SELECT frame,coreset,GROUP_CONCAT(fetype)
FROM fn_frames 
LEFT JOIN fn_fes USING (frameid) 
LEFT JOIN fn_fetypes USING (fetypeid)
LEFT JOIN fn_coretypes USING (coretypeid) 
WHERE frame='Causation' AND coreset IS NOT NULL
GROUP BY coreset;

SELECT 'fe core types' AS subsection;

SELECT 'fe coretypes' AS comment;
SELECT frame,fetype,coretype 
FROM fn_fes
LEFT JOIN fn_frames USING (frameid) 
LEFT JOIN fn_fetypes USING (fetypeid) 
LEFT JOIN fn_coretypes USING (coretypeid) 
ORDER BY frameid,feid
LIMIT 20;

SELECT 'required fe' AS subsection;

SELECT 'required fe' AS comment;
SELECT frame,fet.fetype,rfet.fetype AS requires
FROM fn_fes_required
LEFT JOIN fn_fes AS fe USING (feid)
LEFT JOIN fn_fetypes AS fet USING (fetypeid)
LEFT JOIN fn_fes AS rfe ON (fe2id = rfe.feid)
LEFT JOIN fn_fetypes AS rfet ON (rfe.fetypeid = rfet.fetypeid)
LEFT JOIN fn_frames AS fr ON (fr.frameid = fe.frameid)
ORDER BY fr.frame
LIMIT 20;

SELECT 'excluded fe' AS subsection;

SELECT 'excluded fe' AS comment;
SELECT frame,fet.fetype,xfet.fetype AS excludes
FROM fn_fes_excluded
LEFT JOIN fn_fes AS fe USING (feid)
LEFT JOIN fn_fetypes AS fet USING (fetypeid)
LEFT JOIN fn_fes AS xfe ON (fe2id = xfe.feid)
LEFT JOIN fn_fetypes AS xfet ON (xfe.fetypeid = xfet.fetypeid)
LEFT JOIN fn_frames AS fr ON (fr.frameid = fe.frameid)
ORDER BY fr.frame
LIMIT 20;
