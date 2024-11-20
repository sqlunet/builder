CREATE TABLE IF NOT EXISTS ${pbroles_fnfes.table} (
${pbroles_fnfes.rolesetid} INTEGER NOT NULL,
${pbroles_fnfes.roleid} INTEGER NOT NULL,
${pbroles_fnfes.fnframeid} INTEGER NULL,
${pbroles_fnfes.fnfeid} INTEGER NULL,
${pbroles_fnfes.fnfetypeid} INTEGER NULL,
${pbroles_fnfes.fnframe} VARCHAR(64) NOT NULL,
${pbroles_fnfes.fnfe} VARCHAR(32) NOT NULL
);
