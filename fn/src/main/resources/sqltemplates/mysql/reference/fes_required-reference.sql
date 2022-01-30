${fes_required}.fk1=ALTER TABLE ${fes_required.table} ADD CONSTRAINT fk_${fes_required.table}_${feid} FOREIGN KEY (${feid}) REFERENCES ${fes.table} (${feid});
${fes_required}.fk2=ALTER TABLE ${fes_required.table} ADD CONSTRAINT fk_${fes_required.table}_${fe}2id FOREIGN KEY (${fe}2id) REFERENCES ${fes.table} (${feid});
