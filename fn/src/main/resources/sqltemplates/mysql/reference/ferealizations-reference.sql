ALTER TABLE ${ferealizations.table} ADD CONSTRAINT fk_${ferealizations.table}_${ferealizations.luid} FOREIGN KEY (${ferealizations.luid}) REFERENCES ${lexunits.table} (${lexunits.luid});
ALTER TABLE ${ferealizations.table} ADD CONSTRAINT fk_${ferealizations.table}_${ferealizations.fetypeid} FOREIGN KEY (${ferealizations.fetypeid}) REFERENCES ${fetypes.table} (${fetypes.fetypeid});
