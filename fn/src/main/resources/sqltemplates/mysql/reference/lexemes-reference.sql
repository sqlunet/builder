${lexemes}.fk1=ALTER TABLE ${lexemes.table} ADD CONSTRAINT fk_${lexemes.table}_${luid} FOREIGN KEY (${luid}) REFERENCES ${lexunits.table} (${luid});
${lexemes}.fk2=ALTER TABLE ${lexemes.table} ADD CONSTRAINT fk_${lexemes.table}_${posid} FOREIGN KEY (${posid}) REFERENCES ${poses.table} (${posid});
${lexemes}.fk3=ALTER TABLE ${lexemes.table} ADD CONSTRAINT fk_${lexemes.table}_${fnwordid} FOREIGN KEY (${fnwordid}) REFERENCES ${words.table} (${fnwordid});
