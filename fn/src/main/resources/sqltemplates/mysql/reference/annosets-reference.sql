ALTER TABLE ${annosets.table} ADD CONSTRAINT fk_${annosets.table}_${annosets.sentenceid} FOREIGN KEY (${sannosets.sentenceid}) REFERENCES ${sentences.table} (${sentences.sentenceid});
ALTER TABLE ${annosets.table} ADD CONSTRAINT fk_${annosets.table}_${annosets.frameid} FOREIGN KEY (${annosets.frameid}) REFERENCES ${frames.table} (${frames.frameid});
ALTER TABLE ${annosets.table} ADD CONSTRAINT fk_${annosets.table}_${annosets.luid} FOREIGN KEY (${annosets.luid}) REFERENCES ${lexunits.table} (${lexunits.luid});
ALTER TABLE ${annosets.table} ADD CONSTRAINT fk_${annosets.table}_${annosets.cxnid} FOREIGN KEY (${annosets.cxnid}) REFERENCES ${cxns.table} (${cxns.cxnid});
