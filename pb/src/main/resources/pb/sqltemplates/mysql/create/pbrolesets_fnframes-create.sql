CREATE TABLE IF NOT EXISTS ${pbrolesets_fnframes.table} (
    ${pbrolesets_fnframes.rolesetid} INTEGER NOT NULL,
    ${pbrolesets_fnframes.pos} VARCHAR(1) NOT NULL,
    ${pbrolesets_fnframes.pbwordid} INTEGER NOT NULL,
    ${pbrolesets_fnframes.fnframe} VARCHAR(40) NOT NULL,
    ${pbrolesets_fnframes.fnframeid} INTEGER NULL,
PRIMARY KEY (${pbrolesets_fnframes.rolesetid},${pbrolesets_fnframes.fnframe},${pbrolesets_fnframes.pos},${pbrolesets_fnframes.pbwordid}));

-- PRIMARY KEY (${pbrolesets_fnframes.rolesetid},${pbrolesets_fnframes.fnframeid},${pbrolesets_fnframes.pos},${pbrolesets_fnframes.pbwordid}));
