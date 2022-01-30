${patterns_valenceunits}.fk1=ALTER TABLE ${patterns_valenceunits.table} ADD CONSTRAINT fk_${patterns_valenceunits.table}_${patternid} FOREIGN KEY (${patternid}) REFERENCES ${patterns.table} (${patternid});
${patterns_valenceunits}.fk2=ALTER TABLE ${patterns_valenceunits.table} ADD CONSTRAINT fk_${patterns_valenceunits.table}_${vuid} FOREIGN KEY (${vuid}) REFERENCES ${valenceunits.table} (${vuid});
${patterns_valenceunits}.fk3=ALTER TABLE ${patterns_valenceunits.table} ADD CONSTRAINT fk_${patterns_valenceunits.table}_${feid} FOREIGN KEY (${feid}) REFERENCES ${fes.table} (${feid});
${patterns_valenceunits}.fk4=ALTER TABLE ${patterns_valenceunits.table} ADD CONSTRAINT fk_${patterns_valenceunits.table}_${fetypeid} FOREIGN KEY (${fetypeid}) REFERENCES ${fetypes.table} (${fetypeid});
