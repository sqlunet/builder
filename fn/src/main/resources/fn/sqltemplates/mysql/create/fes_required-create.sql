CREATE TABLE IF NOT EXISTS ${fes_required.table} (
    ${fes_required.feid} INTEGER NOT NULL,
    ${fes_required.fe2id} INTEGER NOT NULL,
PRIMARY KEY (${fes_required.feid},${fes_required.fe2id}) );
