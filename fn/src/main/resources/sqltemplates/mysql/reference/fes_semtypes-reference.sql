ALTER TABLE ${fes_semtypes.table} ADD CONSTRAINT fk_${fes_semtypes.table}_${fes_semtypes.feid} FOREIGN KEY (${fes_semtypes.feid}) REFERENCES ${fes.table} (${fes.feid});
ALTER TABLE ${fes_semtypes.table} ADD CONSTRAINT fk_${fes_semtypes.table}_${fes_semtypes.semtypeid} FOREIGN KEY (${fes_semtypes.semtypeid}) REFERENCES ${semtypes.table} (${semtypes.semtypeid});
