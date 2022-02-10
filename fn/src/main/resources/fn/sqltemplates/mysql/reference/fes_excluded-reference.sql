ALTER TABLE ${fes_excluded.table} ADD CONSTRAINT `fk_@{fes_excluded.table}_@{fes_excluded.feid}` FOREIGN KEY (${fes_excluded.feid}) REFERENCES ${fes.table} (${fes.feid});
ALTER TABLE ${fes_excluded.table} ADD CONSTRAINT `fk_@{fes_excluded.table}_@{fes_excluded.fe2id}` FOREIGN KEY (${fes_excluded.fe2id}) REFERENCES ${fes.table} (${fes.feid});
