DELETE fk
    FROM ${governors_annosets.table} AS fk
    LEFT JOIN ${annosets.table} AS pk ON fk.${governors_annosets.annosetid} = pk.${annosets.annosetid}
WHERE fk.${governors_annosets.annosetid} IS NOT NULL AND pk.${annosets.annosetid} IS NULL;
