-- SELECT *
--     FROM ${grouppatterns_annosets.table} AS fk
-- LEFT JOIN ${annosets.table} AS pk ON fk.${grouppatterns_annosets.annosetid} = pk.${annosets.annosetid}
-- WHERE fk.${grouppatterns_annosets.annosetid} IS NOT NULL AND pk.${annosets.annosetid} IS NULL;

DELETE fk
    FROM ${grouppatterns_annosets.table} AS fk
    LEFT JOIN ${annosets.table} AS pk ON fk.${grouppatterns_annosets.annosetid} = pk.${annosets.annosetid}
WHERE fk.${grouppatterns_annosets.annosetid} IS NOT NULL AND pk.${annosets.annosetid} IS NULL;
