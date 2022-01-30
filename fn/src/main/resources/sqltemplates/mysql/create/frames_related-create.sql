CREATE TABLE IF NOT EXISTS ${frames_related.table} (
    ${frames_related.frameid} INTEGER NOT NULL,
    ${frames_related.frame2id} INTEGER NOT NULL,
    ${frames_related.relationid} INTEGER NOT NULL
);

-- ALTER TABLE ${frames_related.frames_related.table} ADD COLUMN ${frames_related.frame2} VARCHAR(40) AFTER ${frames_related.frame2id};
-- ALTER TABLE ${frames_related.frames_related.table} ADD COLUMN ${frames_related.relation} VARCHAR(40) AFTER ${frames_related.relationid};
