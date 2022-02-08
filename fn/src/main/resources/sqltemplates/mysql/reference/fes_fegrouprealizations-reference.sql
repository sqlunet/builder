ALTER TABLE ${fes_fegrouprealizations.table} ADD CONSTRAINT `fk_@{fes_fegrouprealizations.table}_@{fes_fegrouprealizations.fegrid}` FOREIGN KEY (${fes_fegrouprealizations.fegrid}) REFERENCES ${fegrouprealizations.table} (${fegrouprealizations.fegrid});
ALTER TABLE ${fes_fegrouprealizations.table} ADD CONSTRAINT `fk_@{fes_fegrouprealizations.table}_@{fes_fegrouprealizations.fetypeid}` FOREIGN KEY (${fes_fegrouprealizations.fetypeid}) REFERENCES ${fetypes.table} (${fetypes.fetypeid});
ALTER TABLE ${fes_fegrouprealizations.table} ADD CONSTRAINT `fk_@{fes_fegrouprealizations.table}_@{fes_fegrouprealizations.feid}` FOREIGN KEY (${fes_fegrouprealizations.feid}) REFERENCES ${fes.table} (${fes.feid});