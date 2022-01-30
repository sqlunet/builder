${ferealizations}.fk1=ALTER TABLE ${ferealizations.table} ADD CONSTRAINT fk_${ferealizations.table}_${luid} FOREIGN KEY (${luid}) REFERENCES ${lexunits.table} (${luid});
${ferealizations}.fk2=ALTER TABLE ${ferealizations.table} ADD CONSTRAINT fk_${ferealizations.table}_${fetypeid} FOREIGN KEY (${fetypeid}) REFERENCES ${fetypes.table} (${fetypeid});
