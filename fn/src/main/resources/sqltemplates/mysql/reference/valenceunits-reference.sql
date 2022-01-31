ALTER TABLE ${valenceunits.table} ADD CONSTRAINT fk_${valenceunits.table}_${valenceunits.fetypeid} FOREIGN KEY (${valenceunits.fetypeid}) REFERENCES ${fetypes.table} (${fetypes.fetypeid});
ALTER TABLE ${valenceunits.table} ADD CONSTRAINT fk_${valenceunits.table}_${valenceunits.ptid} FOREIGN KEY (${valenceunits.ptid}) REFERENCES ${pttypes.table} (${pttypes.ptid});
ALTER TABLE ${valenceunits.table} ADD CONSTRAINT fk_${valenceunits.table}_${valenceunits.gfid} FOREIGN KEY (${valenceunits.gfid}) REFERENCES ${gftypes.table} (${gftypes.gfid});
