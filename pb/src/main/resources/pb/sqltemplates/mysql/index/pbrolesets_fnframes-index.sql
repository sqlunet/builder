-- ALTER TABLE ${pbrolesets_fnframes.table} ADD CONSTRAINT `pk_@{pbrolesets_fnframes.table}` PRIMARY KEY                     (${pbrolesets_fnframes.rolesetid},${pbrolesets_fnframes.fnframeid},${pbrolesets_fnframes.pos},${pbrolesets_fnframes.pbwordid}));
ALTER TABLE ${pbrolesets_fnframes.table} ADD KEY        `k_@{pbrolesets_fnframes.table}_@{pbrolesets_fnframes.rolesetid}` (${pbrolesets_fnframes.rolesetid});
ALTER TABLE ${pbrolesets_fnframes.table} ADD KEY        `k_@{pbrolesets_fnframes.table}_@{pbrolesets_fnframes.fnframe}`   (${pbrolesets_fnframes.fnframe});
