SELECT 'FrameNet (annotations)' AS section;

SELECT 'sentences' AS subsection;

SELECT 'sentences matching lexunit' AS comment;
SELECT lexunit,subcorpus,documentdesc,c.corpus,sentenceid,paragno,sentno,`text` 
FROM fn_lexunits 
LEFT JOIN fn_subcorpuses USING (luid)
LEFT JOIN fn_subcorpuses_sentences USING (subcorpusid)
LEFT JOIN fn_sentences AS s USING (sentenceid)
LEFT JOIN fn_documents AS d USING (documentid)
LEFT JOIN fn_corpuses AS c ON (c.corpusid = d.corpusid)
WHERE lexunit = 'communicate.v' AND s.sentenceid IS NOT NULL
ORDER BY c.corpusid,documentid,paragno,sentno;

SELECT 'sentences matching lexunit' AS comment;
SELECT GROUP_CONCAT(`text` SEPARATOR '...') 
FROM fn_lexunits 
LEFT JOIN fn_subcorpuses USING (luid)
LEFT JOIN fn_subcorpuses_sentences USING (subcorpusid)
LEFT JOIN fn_sentences AS s USING (sentenceid)
LEFT JOIN fn_documents AS d USING (documentid)
LEFT JOIN fn_corpuses AS c ON (c.corpusid = d.corpusid)
WHERE lexunit = 'communicate.v' AND s.sentenceid IS NOT NULL
GROUP BY c.corpusid,documentid,paragno
ORDER BY c.corpusid,documentid,paragno;

SELECT 'sentence labels' AS comment;
SELECT `text`,annosetid,layerid,layertype,`start`,`end`,labeltype
FROM fn_sentences
LEFT JOIN fn_annosets AS a USING (sentenceid)
LEFT JOIN fn_layers AS y USING (annosetid)
LEFT JOIN fn_layertypes USING (layertypeid)
LEFT JOIN fn_labels USING (layerid)
LEFT JOIN fn_labeltypes AS l USING (labeltypeid)
WHERE sentenceid = 1225422;

SELECT 'sentence, per layer' AS comment;
SELECT `text`,annosetid,layerid,layertype,GROUP_CONCAT(CONCAT(`start`,'-',`end`,':',labeltype) ORDER BY `start`,`end` SEPARATOR ' , ')
FROM fn_sentences
LEFT JOIN fn_annosets AS a USING (sentenceid)
LEFT JOIN fn_layers AS y USING (annosetid)
LEFT JOIN fn_layertypes USING (layertypeid)
LEFT JOIN fn_labels USING (layerid)
LEFT JOIN fn_labeltypes AS l USING (labeltypeid)
WHERE sentenceid = 1225422
GROUP BY `text`,annosetid,layerid,layertype;

SELECT 'sentence, PENN layer' AS comment;
SELECT `text`,annosetid,layerid,layertype,CONCAT(`start`,'-',`end`,':',labeltype)
FROM fn_sentences
LEFT JOIN fn_annosets AS a USING (sentenceid)
LEFT JOIN fn_layers AS y USING (annosetid)
LEFT JOIN fn_layertypes USING (layertypeid)
LEFT JOIN fn_labels USING (layerid)
LEFT JOIN fn_labeltypes AS l USING (labeltypeid)
WHERE sentenceid = 1225422 AND layertype = 'PENN'
ORDER BY `start`,`end`;

SELECT 'sentence, GF layer' AS comment;
SELECT `text`,annosetid,layerid,layertype,CONCAT(`start`,'-',`end`,':',labeltype)
FROM fn_sentences
LEFT JOIN fn_annosets AS a USING (sentenceid)
LEFT JOIN fn_layers AS y USING (annosetid)
LEFT JOIN fn_layertypes USING (layertypeid)
LEFT JOIN fn_labels USING (layerid)
LEFT JOIN fn_labeltypes AS l USING (labeltypeid)
WHERE sentenceid = 1225422 AND layertype = 'GF'
ORDER BY `start`,`end`;

SELECT 'sentence, PT layer' AS comment;
SELECT `text`,annosetid,layerid,layertype,CONCAT(`start`,'-',`end`,':',labeltype)
FROM fn_sentences
LEFT JOIN fn_annosets AS a USING (sentenceid)
LEFT JOIN fn_layers AS y USING (annosetid)
LEFT JOIN fn_layertypes USING (layertypeid)
LEFT JOIN fn_labels USING (layerid)
LEFT JOIN fn_labeltypes AS l USING (labeltypeid)
WHERE sentenceid = 1225422 AND layertype = 'PT'
ORDER BY `start`,`end`;

SELECT 'sentence, FE layer' AS comment;
SELECT `text`,annosetid,layerid,layertype,fetype,CONCAT(`start`,'-',`end`,':',labeltype)
FROM fn_sentences
LEFT JOIN fn_annosets AS a USING (sentenceid)
LEFT JOIN fn_layers AS y USING (annosetid)
LEFT JOIN fn_layertypes USING (layertypeid)
LEFT JOIN fn_labels USING (layerid)
LEFT JOIN fn_labeltypes AS l USING (labeltypeid)
LEFT JOIN fn_fes USING (feid)
LEFT JOIN fn_fetypes USING (fetypeid)
WHERE sentenceid = 1225422 AND layertype = 'FE'
ORDER BY `start`,`end`;

SELECT 'sentence, Verb layer' AS comment;
SELECT `text`,annosetid,layerid,layertype,CONCAT(`start`,'-',`end`,':',labeltype),SUBSTRING(`text`,`start`+1,`end`-`start`+1)
FROM fn_sentences
LEFT JOIN fn_annosets AS a USING (sentenceid)
LEFT JOIN fn_layers AS y USING (annosetid)
LEFT JOIN fn_layertypes USING (layertypeid)
LEFT JOIN fn_labels USING (layerid)
LEFT JOIN fn_labeltypes AS l USING (labeltypeid)
WHERE annosetid = 1733733 AND layertype = 'Verb'
ORDER BY `start`,`end`;

SELECT 'sentence, Other layer' AS comment;
SELECT `text`,annosetid,layerid,layertype,CONCAT(`start`,'-',`end`,':',labeltype),SUBSTRING(`text`,`start`+1,`end`-`start`+ 1)
FROM fn_sentences
LEFT JOIN fn_annosets AS a USING (sentenceid)
LEFT JOIN fn_layers AS y USING (annosetid)
LEFT JOIN fn_layertypes USING (layertypeid)
LEFT JOIN fn_labels USING (layerid)
LEFT JOIN fn_labeltypes AS l USING (labeltypeid)
LEFT JOIN fn_fes USING (feid)
LEFT JOIN fn_fetypes USING (fetypeid)
WHERE annosetid = 867845 AND layertype = 'Other'
ORDER BY `start`,`end`;

SELECT 'governors' AS subsection;

SELECT 'annoset for governor' AS comment;
SELECT word AS governor,lexunit,`text`,layertype,CONCAT(`start`,'-',`end`,':',labeltype),SUBSTRING(`text`,`start`+1,`end`-`start`+1)
FROM fn_governors 
LEFT JOIN fn_lexunits_governors USING (governorid) 
LEFT JOIN fn_lexunits USING (luid) 
LEFT JOIN fn_words USING (fnwordid) 
LEFT JOIN fn_governors_annosets USING (governorid)
LEFT JOIN fn_annosets USING (annosetid)
LEFT JOIN fn_sentences AS a USING (sentenceid)
LEFT JOIN fn_layers AS y USING (annosetid)
LEFT JOIN fn_layertypes USING (layertypeid)
LEFT JOIN fn_labels USING (layerid)
LEFT JOIN fn_labeltypes AS l USING (labeltypeid)
WHERE lexunit = 'gossip.n' AND labelid IS NOT NULL 
ORDER BY word
LIMIT 10;

SELECT 'valence units' AS subsection;
SELECT 'text occurrence of "Circumstances" FE for lexunit "avoid.v" from frame "Avoiding"' AS comment;
SELECT lexunit,ferid,fetype,pt,gf,SUBSTRING(`text`,`start`+1,`end`-`start`+1) AS occurrence,`text`
FROM fn_frames
LEFT JOIN fn_lexunits USING (frameid)
LEFT JOIN fn_ferealizations AS r USING (luid)
LEFT JOIN fn_valenceunits USING (fetypeid)
LEFT JOIN fn_fetypes USING (fetypeid)
LEFT JOIN fn_gftypes USING (gfid)
LEFT JOIN fn_pttypes USING (ptid)
LEFT JOIN fn_valenceunits_annosets USING (vuid)
LEFT JOIN fn_annosets USING (annosetid)
LEFT JOIN fn_sentences AS a USING (sentenceid)
LEFT JOIN fn_layers AS y USING (annosetid)
LEFT JOIN fn_layertypes USING (layertypeid)
LEFT JOIN fn_labels USING (layerid)
LEFT JOIN fn_labeltypes AS l USING (labeltypeid)
WHERE frame = 'Avoiding' AND lexunit = 'avoid.v' AND fetype = 'Circumstances' AND layertype = 'FE' AND labeltype = 'Circumstances'
ORDER BY ferid;

SELECT 'text occurrence of "Manner" FE for lexunit "avoid.v" from frame "Avoiding"' AS comment;
SELECT lexunit,fetype,pt,gf,SUBSTRING(`text`,`start`+1,`end`-`start`+1) AS occurrence,`text`
FROM fn_frames
LEFT JOIN fn_lexunits USING (frameid)
LEFT JOIN fn_ferealizations AS r USING (luid)
LEFT JOIN fn_valenceunits USING (fetypeid)
LEFT JOIN fn_fetypes USING (fetypeid)
LEFT JOIN fn_gftypes USING (gfid)
LEFT JOIN fn_pttypes USING (ptid)
LEFT JOIN fn_valenceunits_annosets USING (vuid)
LEFT JOIN fn_annosets USING (annosetid)
LEFT JOIN fn_sentences AS a USING (sentenceid)
LEFT JOIN fn_layers AS y USING (annosetid)
LEFT JOIN fn_layertypes USING (layertypeid)
LEFT JOIN fn_labels USING (layerid)
LEFT JOIN fn_labeltypes AS l USING (labeltypeid)
WHERE frame = 'Avoiding' AND lexunit = 'avoid.v' AND fetype = 'Manner' AND layertype = 'FE' AND labeltype = 'Manner'
ORDER BY ferid;

SELECT 'text occurrence of "Manner" FE for lexunit "avoid.v" from frame "Avoiding"' AS comment;
SELECT lexunit,fetype,pt,gf,SUBSTRING(`text`,`start`+1,`end`-`start`+1) AS occurrence,`text`
FROM fn_frames
LEFT JOIN fn_lexunits USING (frameid)
LEFT JOIN fn_ferealizations AS r USING (luid)
LEFT JOIN fn_valenceunits USING (fetypeid)
LEFT JOIN fn_fetypes USING (fetypeid)
LEFT JOIN fn_gftypes USING (gfid)
LEFT JOIN fn_pttypes USING (ptid)
LEFT JOIN fn_valenceunits_annosets USING (vuid)
LEFT JOIN fn_annosets USING (annosetid)
LEFT JOIN fn_sentences AS a USING (sentenceid)
LEFT JOIN fn_layers AS y USING (annosetid)
LEFT JOIN fn_layertypes USING (layertypeid)
LEFT JOIN fn_labels USING (layerid)
LEFT JOIN fn_labeltypes AS l USING (labeltypeid)
WHERE frame = 'Avoiding' AND lexunit = 'avoid.v' AND fetype = 'Agent' AND layertype = 'FE' AND labeltype = 'Agent'  AND gfid IS NOT NULL
ORDER BY ferid;
