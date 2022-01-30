${fes}.fk1=ALTER TABLE ${fes.table} ADD CONSTRAINT fk_${fes.table}_${frameid} FOREIGN KEY (${frameid}) REFERENCES ${frames.table} (${frameid});
${fes}.fk2=ALTER TABLE ${fes.table} ADD CONSTRAINT fk_${fes.table}_${fetypeid} FOREIGN KEY (${fetypeid}) REFERENCES ${fetypes.table} (${fetypeid});
${fes}.fk3=ALTER TABLE ${fes.table} ADD CONSTRAINT fk_${fes.table}_${coretypeid} FOREIGN KEY (${coretypeid}) REFERENCES ${coretypes.table} (${coretypeid});
