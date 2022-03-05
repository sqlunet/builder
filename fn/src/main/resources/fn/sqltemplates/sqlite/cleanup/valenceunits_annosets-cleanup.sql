DELETE fk
FROM ${valenceunits_annosets.table} AS fk
LEFT JOIN ${annosets.table} AS pk ON fk.${valenceunits_annosets.annosetid} = pk.${annosets.annosetid}
WHERE fk.${valenceunits_annosets.annosetid} IS NOT NULL AND pk.${annosets.annosetid} IS NULL;
