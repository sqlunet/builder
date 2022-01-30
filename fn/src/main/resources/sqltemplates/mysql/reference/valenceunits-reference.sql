${valenceunits}.fk1=ALTER TABLE ${valenceunits.table} ADD CONSTRAINT fk_${valenceunits.table}_${ferid} FOREIGN KEY (${ferid}) REFERENCES ${ferealizations.table} (${ferid});
${valenceunits}.fk2=ALTER TABLE ${valenceunits.table} ADD CONSTRAINT fk_${valenceunits.table}_${ptid} FOREIGN KEY (${ptid}) REFERENCES ${pttypes.table} (${ptid});
${valenceunits}.fk3=ALTER TABLE ${valenceunits.table} ADD CONSTRAINT fk_${valenceunits.table}_${gfid} FOREIGN KEY (${gfid}) REFERENCES ${gftypes.table} (${gfid});
