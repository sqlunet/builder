ALTER TABLE ${layers.table} ADD CONSTRAINT `fk_@{layers.table}_@{layers.annosetid}` FOREIGN KEY (${layers.annosetid}) REFERENCES ${annosets.table} (${annosets.annosetid});
ALTER TABLE ${layers.table} ADD CONSTRAINT `fk_@{layers.table}_@{layers.layertypeid}` FOREIGN KEY (${layers.layertypeid}) REFERENCES ${layertypes.table} (${layertypes.layertypeid});
