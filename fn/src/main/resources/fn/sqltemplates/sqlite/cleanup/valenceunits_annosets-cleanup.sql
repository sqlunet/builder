DELETE
FROM ${valenceunits_annosets.table} AS fk2
WHERE fk2.${valenceunits_annosets.annosetid} IN (
    SELECT fk.${valenceunits_annosets.annosetid}
    FROM ${valenceunits_annosets.table} AS fk
    LEFT JOIN ${annosets.table} AS pk ON fk.${valenceunits_annosets.annosetid} = pk.${annosets.annosetid}
    WHERE fk.${valenceunits_annosets.annosetid} IS NOT NULL AND pk.${annosets.annosetid} IS NULL);
