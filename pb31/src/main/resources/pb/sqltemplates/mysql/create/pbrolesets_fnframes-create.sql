CREATE TABLE IF NOT EXISTS ${pbrolesets_fnframes.table} (
    ${pbrolesets_fnframes.rolesetid} INTEGER     NOT NULL,
    ${pbrolesets_fnframes.fnframeid} INTEGER     NULL,
    ${pbrolesets_fnframes.pos}       VARCHAR(1)  NOT NULL,
    ${pbrolesets_fnframes.pbwordid}  INTEGER     NOT NULL,
    ${pbrolesets_fnframes.fnframe}   VARCHAR(40) NOT NULL
);
