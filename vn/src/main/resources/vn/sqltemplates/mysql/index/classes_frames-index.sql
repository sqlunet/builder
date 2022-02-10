ALTER TABLE ${classes_frames.table} ADD CONSTRAINT `uniq_@{classes_frames.table}_@{classes_frames.classid}_@{classes_frames.frameid}` UNIQUE KEY (${classes_frames.classid},${classes_frames.frameid});
ALTER TABLE ${classes_frames.table} ADD KEY `k_@{classes_frames.table}_@{classes_frames.classid}` (${classes_frames.classid});
ALTER TABLE ${classes_frames.table} ADD KEY `k_@{classes_frames.table}_@{classes_frames.frameid}` (${classes_frames.frameid});
