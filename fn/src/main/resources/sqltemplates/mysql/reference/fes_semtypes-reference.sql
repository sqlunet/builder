${fes_semtypes}.fk1=ALTER TABLE ${fes_semtypes.table} ADD CONSTRAINT fk_${fes_semtypes.table}_${feid} FOREIGN KEY (${feid}) REFERENCES ${fes.table} (${feid});
${fes_semtypes}.fk2=ALTER TABLE ${fes_semtypes.table} ADD CONSTRAINT fk_${fes_semtypes.table}_${semtypeid} FOREIGN KEY (${semtypeid}) REFERENCES ${semtypes.table} (${semtypeid});
