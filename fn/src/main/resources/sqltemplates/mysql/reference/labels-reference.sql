ALTER TABLE ${labels.table} ADD CONSTRAINT fk_${labels.table}_${labels.layerid} FOREIGN KEY (${labels.layerid}) REFERENCES ${layers.table} (${layers.layerid});
ALTER TABLE ${labels.table} ADD CONSTRAINT fk_${labels.table}_${labels.feid} FOREIGN KEY (${labels.feid}) REFERENCES ${fes.table} (${fes.feid});
ALTER TABLE ${labels.table} ADD CONSTRAINT fk_${labels.table}_${labels.labeltypeid} FOREIGN KEY (${labels.labeltypeid}) REFERENCES ${labeltypes.table} (${labeltypes.labeltypeid});
ALTER TABLE ${labels.table} ADD CONSTRAINT fk_${labels.table}_${labels.labelitypeid} FOREIGN KEY (${labels.labelitypeid}) REFERENCES ${labelitypes.table} (${labelitypes.labelitypeid});
