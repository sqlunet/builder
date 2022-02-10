ALTER TABLE ${lexunits.table} ADD CONSTRAINT `fk_@{lexunits.table}_@{lexunits.frameid}` FOREIGN KEY (${lexunits.frameid}) REFERENCES ${frames.table} (${frames.frameid});
ALTER TABLE ${lexunits.table} ADD CONSTRAINT `fk_@{lexunits.table}_@{lexunits.posid}` FOREIGN KEY (${lexunits.posid}) REFERENCES ${poses.table} (${poses.posid});
-- ALTER TABLE ${lexunits.table} ADD CONSTRAINT `fk_@{lexunits.table}_@{lexunits.incorporatedfeid}` FOREIGN KEY (${lexunits.incorporatedfeid}) REFERENCES ${fes.table} (${fes.feid});
ALTER TABLE ${lexunits.table} ADD CONSTRAINT `fk_@{lexunits.table}_@{lexunits.incorporatedfetypeid}` FOREIGN KEY (${lexunits.incorporatedfetypeid}) REFERENCES ${fetypes.table} (${fetypes.fetypeid});
