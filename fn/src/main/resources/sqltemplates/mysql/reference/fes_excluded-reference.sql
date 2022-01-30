${fes_excluded}.fk1=ALTER TABLE ${fes_excluded.table} ADD CONSTRAINT fk_${fes_excluded.table}_${feid} FOREIGN KEY (${feid}) REFERENCES ${fes.table} (${feid});
${fes_excluded}.fk2=ALTER TABLE ${fes_excluded.table} ADD CONSTRAINT fk_${fes_excluded.table}_${fe}2id FOREIGN KEY (${fe}2id) REFERENCES ${fes.table} (${feid});
