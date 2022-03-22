CREATE TABLE IF NOT EXISTS ${lexemes.table} (
    ${lexemes.lexemeid} INTEGER NOT NULL,
    ${lexemes.luid} INTEGER,
    ${lexemes.fnwordid} INTEGER,
    ${lexemes.posid} INTEGER,
    ${lexemes.breakbefore} BOOLEAN,
    ${lexemes.headword} BOOLEAN,
    ${lexemes.lexemeidx} INTEGER DEFAULT NULL
);
