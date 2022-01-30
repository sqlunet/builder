${frames_related}.fk1=ALTER TABLE ${frames_related.table} ADD CONSTRAINT fk_${frames_related.table}_${frameid} FOREIGN KEY (${frameid}) REFERENCES ${frames.table} (${frameid});
${frames_related}.fk2=ALTER TABLE ${frames_related.table} ADD CONSTRAINT fk_${frames_related.table}_${frame}2id FOREIGN KEY (${frame}2id) REFERENCES ${frames.table} (${frameid});
${frames_related}.fk3=ALTER TABLE ${frames_related.table} ADD CONSTRAINT fk_${frames_related.table}_${relationid} FOREIGN KEY (${relationid}) REFERENCES ${framerelations.table} (${relationid});
