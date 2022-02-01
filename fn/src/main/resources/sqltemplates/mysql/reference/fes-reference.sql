ALTER TABLE ${fes.table} ADD CONSTRAINT `fk_@{fes.table}_@{fes.frameid}` FOREIGN KEY (${fes.frameid}) REFERENCES ${frames.table} (${frames.frameid});
ALTER TABLE ${fes.table} ADD CONSTRAINT `fk_@{fes.table}_@{fes.fetypeid}` FOREIGN KEY (${fes.fetypeid}) REFERENCES ${fetypes.table} (${fetypes.fetypeid});
ALTER TABLE ${fes.table} ADD CONSTRAINT `fk_@{fes.table}_@{fes.coretypeid}` FOREIGN KEY (${fes.coretypeid}) REFERENCES ${coretypes.table} (${coretypes.coretypeid});
