${semtypes_supers}.fk1=ALTER TABLE ${semtypes_supers.table} ADD CONSTRAINT fk_${semtypes_supers.table}_${semtypeid} FOREIGN KEY (${semtypeid}) REFERENCES ${semtypes.table} (${semtypeid});
${semtypes_supers}.fk2=ALTER TABLE ${semtypes_supers.table} ADD CONSTRAINT fk_${semtypes_supers.table}_${supersemtypeid} FOREIGN KEY (${supersemtypeid}) REFERENCES ${semtypes.table} (${semtypeid});
