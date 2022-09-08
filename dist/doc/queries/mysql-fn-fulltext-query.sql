SELECT 'FrameNet (fulltext)' AS section;

SELECT 'corpuses and documents' AS subsection;

SELECT 'number of corpuses' AS comment;
SELECT COUNT(*)
FROM fn_corpuses;

SELECT 'number of documents' AS comment;
SELECT COUNT(*)
FROM fn_documents;

SELECT 'distribution of documents per corpus' AS comment;
SELECT corpus,COUNT(*)
FROM fn_corpuses
LEFT JOIN fn_documents USING (corpusid)
GROUP BY corpusid
ORDER BY corpus;

SELECT 'distribution of sentences per document' AS comment;
SELECT d.corpusid,s.corpusid,documentdesc,COUNT(*) AS c
FROM fn_documents AS d
LEFT JOIN fn_sentences AS s USING (documentid)
GROUP BY documentid,documentdesc,d.corpusid,s.corpusid,documentdesc
ORDER BY c DESC
LIMIT 20;

SELECT 'distribution of sentences and documents per corpus' AS comment;
SELECT corpus,SUM(c) AS nsentences,COUNT(documentdesc) AS ndocuments,SUBSTRING(GROUP_CONCAT(documentdesc ORDER BY documentdesc),1,80) FROM (
SELECT d.corpusid,documentdesc,COUNT(*) AS c
FROM fn_documents AS d
LEFT JOIN fn_sentences AS s USING (documentid)
GROUP BY documentid
) AS s
LEFT JOIN fn_corpuses USING (corpusid)
GROUP BY s.corpusid
ORDER BY nsentences DESC;
