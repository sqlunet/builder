ALTER TABLE ${pms.table} ADD KEY 'k_@{pms.table}_@{pms.pmroleid}' (${pms.pmroleid});
ALTER TABLE ${pms.table} ADD KEY 'k_@{pms.table}_@{pms.synsetid}' (${pms.synsetid});
ALTER TABLE ${pms.table} ADD KEY 'k_@{pms.table}_@{pms.wordid}'   (${pms.wordid});

