${lexunits}.fk1=ALTER TABLE ${lexunits.table} ADD CONSTRAINT fk_${lexunits.table}_${frameid} FOREIGN KEY (${frameid}) REFERENCES ${frames.table} (${frameid});
${lexunits}.fk2=ALTER TABLE ${lexunits.table} ADD CONSTRAINT fk_${lexunits.table}_${posid} FOREIGN KEY (${posid}) REFERENCES ${poses.table} (${posid});
${lexunits}.fk3=ALTER TABLE ${lexunits.table} ADD CONSTRAINT fk_${lexunits.table}_${incorporatedfeid} FOREIGN KEY (${incorporatedfeid}) REFERENCES ${fes.table} (${feid});
