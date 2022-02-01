ALTER TABLE ${fes_required.table} ADD CONSTRAINT `fk_@{fes_required.table}_@{fes_required.feid}` FOREIGN KEY (${fes_required.feid}) REFERENCES ${fes.table} (${fes.feid});
ALTER TABLE ${fes_required.table} ADD CONSTRAINT `fk_@{fes_required.table}_@{fes_required.fe2id}` FOREIGN KEY (${fes_required.fe2id}) REFERENCES ${fes.table} (${fes.feid});
