${lexunits_semtypes}.fk1=ALTER TABLE ${lexunits_semtypes.table} ADD CONSTRAINT fk_${lexunits_semtypes.table}_${luid} FOREIGN KEY (${luid}) REFERENCES ${lexunits.table} (${luid});
${lexunits_semtypes}.fk2=ALTER TABLE ${lexunits_semtypes.table} ADD CONSTRAINT fk_${lexunits_semtypes.table}_${semtypeid} FOREIGN KEY (${semtypeid}) REFERENCES ${semtypes.table} (${semtypeid});
