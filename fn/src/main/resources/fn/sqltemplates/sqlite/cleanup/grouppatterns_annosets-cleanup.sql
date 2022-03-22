DELETE
FROM ${grouppatterns_annosets.table} AS fk2
WHERE fk2.${grouppatterns_annosets.annosetid} IN (
    SELECT fk.${grouppatterns_annosets.annosetid}
    FROM ${grouppatterns_annosets.table} AS fk
    LEFT JOIN ${annosets.table} AS pk ON fk.${grouppatterns_annosets.annosetid} = pk.${annosets.annosetid}
    WHERE fk.${grouppatterns_annosets.annosetid} IS NOT NULL AND pk.${annosets.annosetid} IS NULL);
