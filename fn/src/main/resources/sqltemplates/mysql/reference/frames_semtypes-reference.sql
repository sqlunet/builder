${frames_semtypes}.fk1=ALTER TABLE ${frames_semtypes.table} ADD CONSTRAINT fk_${frames_semtypes.table}_${frameid} FOREIGN KEY (${frameid}) REFERENCES ${frames.table} (${frameid});
${frames_semtypes}.fk2=ALTER TABLE ${frames_semtypes.table} ADD CONSTRAINT fk_${frames_semtypes.table}_${semtypeid} FOREIGN KEY (${semtypeid}) REFERENCES ${semtypes.table} (${semtypeid});
