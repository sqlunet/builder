ALTER TABLE ${classes_frames.table} ADD CONSTRAINT fk_${classes_frames.table}_${classes_frames.classid} FOREIGN KEY (${classes_frames.classid}) REFERENCES ${classes.table} (${classes.classid});
ALTER TABLE ${classes_frames.table} ADD CONSTRAINT fk_${classes_frames.table}_${classes_frames.frameid} FOREIGN KEY (${classes_frames.frameid}) REFERENCES ${frames.table} (${frames.frameid});
