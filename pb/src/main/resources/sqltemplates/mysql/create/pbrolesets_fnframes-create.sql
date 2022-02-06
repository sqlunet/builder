CREATE TABLE IF NOT EXISTS ${pbrolesets_fnframes.table} (
    ${pbrolesets_fnframes.rolesetid} INTEGER NOT NULL,
    ${pbrolesets_fnframesfnframeid} INTEGER NOT NULL,
    ${pbrolesets_fnframespos} VARCHAR(1) NOT NULL,
    ${pbrolesets_fnframespbwordid} INTEGER NOT NULL,
PRIMARY KEY (${pbrolesets_fnframes.rolesetid},${pbrolesets_fnframes.fnframeid},${pbrolesets_fnframes.pos},${pbrolesets_fnframes.pbwordid}));
