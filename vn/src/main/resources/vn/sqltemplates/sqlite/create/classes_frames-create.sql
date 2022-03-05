CREATE TABLE IF NOT EXISTS ${classes_frames.table} (
${classes_frames.classid} INTEGER NOT NULL,
${classes_frames.frameid} INTEGER NOT NULL,
PRIMARY KEY (${classes_frames.classid},${classes_frames.frameid}));
