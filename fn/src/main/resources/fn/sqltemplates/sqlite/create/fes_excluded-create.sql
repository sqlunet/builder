CREATE TABLE IF NOT EXISTS ${fes_excluded.table} (
${fes_excluded.feid} INTEGER NOT NULL,
${fes_excluded.fe2id} INTEGER NOT NULL,
PRIMARY KEY (${fes_excluded.feid},${fes_excluded.fe2id}) );
