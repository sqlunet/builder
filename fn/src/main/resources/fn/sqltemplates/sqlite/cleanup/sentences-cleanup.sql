UPDATE
${sentences.table} AS fk
LEFT JOIN ${documents.table} AS pk ON fk.${sentences.documentid} = pk.${documents.documentid}
SET fk.${sentences.documentid} = NULL
WHERE fk.${sentences.documentid} IS NOT NULL AND pk.${documents.documentid} IS NULL;
UPDATE
${sentences.table} AS fk
LEFT JOIN ${corpuses.table} AS pk ON fk.${sentences.corpusid} = pk.${corpuses.corpusid}
SET fk.${sentences.corpusid} = NULL
WHERE fk.${sentences.corpusid} IS NOT NULL AND pk.${corpuses.corpusid} IS NULL;
