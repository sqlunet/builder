${layers}.fk1=ALTER TABLE ${layers.table} ADD CONSTRAINT fk_${layers.table}_${annosetid} FOREIGN KEY (${annosetid}) REFERENCES ${annosets.table} (${annosetid});
${layers}.fk2=ALTER TABLE ${layers.table} ADD CONSTRAINT fk_${layers.table}_${layertypeid} FOREIGN KEY (${layertypeid}) REFERENCES ${layertypes.table} (${layertypeid});
