ALTER TABLE ${valenceunits.table} ADD CONSTRAINT fk_${valenceunits.table}_${valenceunits.ferid} FOREIGN KEY (${valenceunits.ferid}) REFERENCES ${ferealizations.table} (${ferealizations.ferid});
ALTER TABLE ${valenceunits.table} ADD CONSTRAINT fk_${valenceunits.table}_${valenceunits.ptid} FOREIGN KEY (${valenceunits.ptid}) REFERENCES ${pttypes.table} (${pttypes.ptid});
ALTER TABLE ${valenceunits.table} ADD CONSTRAINT fk_${valenceunits.table}_${valenceunits.gfid} FOREIGN KEY (${valenceunits.gfid}) REFERENCES ${gftypes.table} (${gftypes.gfid});
