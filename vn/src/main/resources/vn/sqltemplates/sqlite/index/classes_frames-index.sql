CREATE UNIQUE INDEX `pk_@{classes_frames.table}` ON ${classes_frames.table} (${classes_frames.classid},${classes_frames.frameid});
CREATE UNIQUE INDEX `uk_@{classes_frames.table}_@{classes_frames.classid}_@{classes_frames.frameid}` ON ${classes_frames.table} (${classes_frames.classid},${classes_frames.frameid});
CREATE INDEX `k_@{classes_frames.table}_@{classes_frames.classid}` ON ${classes_frames.table} (${classes_frames.classid});
CREATE INDEX `k_@{classes_frames.table}_@{classes_frames.frameid}` ON ${classes_frames.table} (${classes_frames.frameid});
