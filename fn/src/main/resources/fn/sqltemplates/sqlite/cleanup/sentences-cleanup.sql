UPDATE
${sentences.table} AS fk2
SET ${sentences.documentid} = NULL
WHERE fk2.${sentences.documentid} IN (
    SELECT fk.${sentences.documentid}
    FROM ${sentences.table} AS fk
    LEFT JOIN ${documents.table} AS pk ON fk.${sentences.documentid} = pk.${documents.documentid}
    WHERE fk.${sentences.documentid} IS NOT NULL AND pk.${documents.documentid} IS NULL);

UPDATE
${sentences.table} AS fk2
SET ${sentences.corpusid} = NULL
WHERE fk2.${sentences.corpusid} IN (
    SELECT fk.${sentences.corpusid}
    FROM ${sentences.table} AS fk
    LEFT JOIN ${corpuses.table} AS pk ON fk.${sentences.corpusid} = pk.${corpuses.corpusid}
    WHERE fk.${sentences.corpusid} IS NOT NULL AND pk.${corpuses.corpusid} IS NULL);
