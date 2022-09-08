SELECT 'FrameNet (FE, FE realizations)' AS section;

SELECT 'fe realizations' AS subsection;

SELECT 'fe realizations of lexunit "avoid.v" from frame "Avoiding"' AS comment;
SELECT lexunit,ferid,fetype,pt,gf,total AS annotated
FROM fn_frames
LEFT JOIN fn_lexunits USING (frameid)
LEFT JOIN fn_ferealizations AS r USING (luid)
LEFT JOIN fn_valenceunits USING (fetypeid)
LEFT JOIN fn_fetypes USING (fetypeid)
LEFT JOIN fn_gftypes USING (gfid)
LEFT JOIN fn_pttypes USING (ptid)
WHERE frame = 'Avoiding' AND lexunit = 'avoid.v'
ORDER BY ferid;

SELECT lexunit,ferid,fetype,CONCAT(pt,'.',IFNULL(gf, '--')) AS realization,total AS annotated
FROM fn_frames
LEFT JOIN fn_lexunits USING (frameid)
LEFT JOIN fn_ferealizations AS r USING (luid)
LEFT JOIN fn_valenceunits USING (fetypeid)
LEFT JOIN fn_fetypes USING (fetypeid)
LEFT JOIN fn_gftypes USING (gfid)
LEFT JOIN fn_pttypes USING (ptid)
WHERE frame = 'Avoiding' AND lexunit = 'avoid.v'
ORDER BY ferid;

SELECT 'fe realizations of lexunit "avoid.v" from frame "Avoiding" grouped by realization' AS comment;
SELECT lexunit,ferid,fetype,GROUP_CONCAT(CONCAT(pt,'.',IFNULL(gf, '--'))) AS realizations,total AS annotated
FROM fn_frames
LEFT JOIN fn_lexunits USING (frameid)
LEFT JOIN fn_ferealizations AS r USING (luid)
LEFT JOIN fn_valenceunits USING (fetypeid)
LEFT JOIN fn_fetypes USING (fetypeid)
LEFT JOIN fn_gftypes USING (gfid)
LEFT JOIN fn_pttypes USING (ptid)
WHERE frame = 'Avoiding' AND lexunit = 'avoid.v'
GROUP BY ferid
ORDER BY ferid;

SELECT 'fe across frames' AS subsection;
SELECT 'distribution of fe across frames' AS comment;
SELECT fetype,GROUP_CONCAT(frame)
FROM fn_fes 
LEFT JOIN fn_fetypes USING (fetypeid)
LEFT JOIN fn_frames USING (frameid)
GROUP BY fetypeid
LIMIT 20;

SELECT 'incorporated fe' AS subsection;
SELECT 'incorporated fe' AS comment;
SELECT frame,lexunit,luid,fetype AS incorporatedfe,ludefinition
FROM fn_lexunits AS u
LEFT JOIN fn_fetypes AS t ON (t.fetypeid = u.incorporatedfetypeid)
LEFT JOIN fn_frames USING (frameid)
WHERE incorporatedfetypeid IS NOT NULL
ORDER BY frameid
LIMIT 20;

SELECT 'incorporated fe' AS comment;
SELECT frame,lexunit,luid,ludefinition,feid,fetype AS incorporatedfe,feabbrev,coreset,fedefinition
FROM fn_lexunits AS u
LEFT JOIN fn_fes AS e ON (e.fetypeid = u.incorporatedfetypeid AND e.frameid = u.frameid)
LEFT JOIN fn_fetypes USING (fetypeid)
LEFT JOIN fn_frames AS f ON (u.frameid = f.frameid)
WHERE f.frame = 'Operate_vehicle' AND incorporatedfetypeid IS NOT NULL;

SELECT 'FE group realizations, valence units' AS subsection;
SELECT 'fe group realizations, valence units' AS comment;
SELECT lexunit,fegrid,patternid,fetype,CONCAT(pt,'.',IFNULL(gf, '--')) AS realizations
FROM fn_frames
LEFT JOIN fn_lexunits USING (frameid)
LEFT JOIN fn_fegrouprealizations AS r USING (luid)
LEFT JOIN fn_grouppatterns AS g USING (fegrid)
LEFT JOIN fn_grouppatterns_patterns AS pgp USING (patternid)
LEFT JOIN fn_ferealizations_valenceunits AS p USING (ferid,vuid)
LEFT JOIN fn_valenceunits USING (vuid)
LEFT JOIN fn_fetypes USING (fetypeid)
LEFT JOIN fn_gftypes USING (gfid)
LEFT JOIN fn_pttypes USING (ptid)
WHERE frame = 'Avoiding' AND lexunit = 'avoid.v'
ORDER BY fegrid,patternid,vuid;

SELECT 'fe group realizations, by patterns' AS subsection;
SELECT lexunit,fegrid,patternid,g.total AS annotated,GROUP_CONCAT(CONCAT(fetype,'.',pt,'.',IFNULL(gf, '--')) ORDER BY vuid) AS realizations
FROM fn_frames
LEFT JOIN fn_lexunits USING (frameid)
LEFT JOIN fn_fegrouprealizations AS r USING (luid)
LEFT JOIN fn_grouppatterns AS g USING (fegrid)
LEFT JOIN fn_grouppatterns_patterns AS pgp USING (patternid)
LEFT JOIN fn_ferealizations_valenceunits AS p USING (ferid,vuid)
LEFT JOIN fn_valenceunits USING (vuid)
LEFT JOIN fn_fetypes USING (fetypeid)
LEFT JOIN fn_gftypes USING (gfid)
LEFT JOIN fn_pttypes USING (ptid)
WHERE frame = 'Avoiding' AND lexunit = 'avoid.v'
GROUP BY lexunit,fegrid,patternid,g.total
ORDER BY fegrid,patternid;

SELECT 'fe group realizations, by group realization' AS subsection;
SELECT 'fe group realizations, by group realization' AS comment;
SELECT lexunit,fegrid,GROUP_CONCAT(realizations SEPARATOR '  |  '),SUM(annotated) FROM (
SELECT lexunit,fegrid,patternid,g.total AS annotated,GROUP_CONCAT(CONCAT(fetype,'.',pt,'.',IFNULL(gf, '--')) ORDER BY vuid) AS realizations
FROM fn_frames
LEFT JOIN fn_lexunits USING (frameid)
LEFT JOIN fn_fegrouprealizations AS r USING (luid)
LEFT JOIN fn_grouppatterns AS g USING (fegrid)
LEFT JOIN fn_grouppatterns_patterns AS pgp USING (patternid)
LEFT JOIN fn_ferealizations_valenceunits AS p USING (ferid,vuid)
LEFT JOIN fn_valenceunits USING (vuid)
LEFT JOIN fn_fetypes USING (fetypeid)
LEFT JOIN fn_gftypes USING (gfid)
LEFT JOIN fn_pttypes USING (ptid)
WHERE frame = 'Avoiding' AND lexunit = 'avoid.v'
GROUP BY lexunit,fegrid,patternid,annotated
ORDER BY fegrid,patternid
) AS pat
GROUP BY lexunit,fegrid;
