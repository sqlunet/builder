${labels}.fk1=ALTER TABLE ${labels.table} ADD CONSTRAINT fk_${labels.table}_${layerid} FOREIGN KEY (${layerid}) REFERENCES ${layers.table} (${layerid});
${labels}.fk2=ALTER TABLE ${labels.table} ADD CONSTRAINT fk_${labels.table}_${feid} FOREIGN KEY (${feid}) REFERENCES ${fes.table} (${feid});
${labels}.fk3=ALTER TABLE ${labels.table} ADD CONSTRAINT fk_${labels.table}_${labeltypeid} FOREIGN KEY (${labeltypeid}) REFERENCES ${labeltypes.table} (${labeltypeid});
${labels}.fk4=ALTER TABLE ${labels.table} ADD CONSTRAINT fk_${labels.table}_${labelitypeid} FOREIGN KEY (${labelitypeid}) REFERENCES ${labelitypes.table} (${labelitypeid});
