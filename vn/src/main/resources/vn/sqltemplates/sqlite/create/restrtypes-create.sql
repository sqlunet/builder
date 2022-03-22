CREATE TABLE IF NOT EXISTS ${restrtypes.table} (
${restrtypes.restrid} INTEGER NOT NULL,
${restrtypes.restrval} VARCHAR (1) NOT NULL,
${restrtypes.restr} VARCHAR (32) NOT NULL,
${restrtypes.issyn} BOOLEAN NOT NULL DEFAULT FALSE
);
