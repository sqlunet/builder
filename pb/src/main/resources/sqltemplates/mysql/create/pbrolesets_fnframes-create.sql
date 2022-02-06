CREATE TABLE IF NOT EXISTS ${pbrolesets_fnframes.table} (
    ${pbrolesets_fnframes.rolesetid} INTEGER NOT NULL,
    ${pbrolesets_fnframes.frameid} INTEGER NOT NULL,
    ${pbrolesets_fnframes.pos} VARCHAR(1) NOT NULL,
    ${pbrolesets_fnframes.pbwordid} INTEGER NOT NULL,
PRIMARY KEY (${pbrolesets_fnframes.rolesetid},${pbrolesets_fnframes.frameid},${pbrolesets_fnframes.pos},${pbrolesets_fnframes.pbwordid}));
