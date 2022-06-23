-- ALTER TABLE ${pms.table} ADD CONSTRAINT `fk_@{pms.table}_@{pms.wordid}'      FOREIGN KEY (${pms.wordid})         REFERENCES ${wnwords.table}    (${wnwords.wordid});
-- ALTER TABLE ${pms.table} ADD CONSTRAINT `fk_@{pms.table}_@{pms.synsetid}'    FOREIGN KEY (${pms.synsetid})       REFERENCES ${wnsynsets.table}  (${wnsynsets.synsetid});

-- ALTER TABLE ${pms.table} ADD CONSTRAINT `fk_@{pms.table}_@{pms.vnclassid}'   FOREIGN KEY (${pms.vnclassid})      REFERENCES ${vnclasses.table}  (${vnclasses.classid});
-- ALTER TABLE ${pms.table} ADD CONSTRAINT `fk_@{pms.table}_@{pms.vnroleid}'    FOREIGN KEY (${pms.vnroleid})       REFERENCES ${vnroles.table}    (${vnroles.roleid});
-- ALTER TABLE ${pms.table} ADD CONSTRAINT `fk_@{pms.table}_@{pms.vnwordid}'    FOREIGN KEY (${pms.vnwordid}        REFERENCES ${vnwords.table}    (${vnwords.vnwordid});

-- ALTER TABLE ${pms.table} ADD CONSTRAINT `fk_@{pms.table}_@{pms.pbrolesetid}' FOREIGN KEY (${pms.pbrolesetid})    REFERENCES ${pbrolesets.table} (${pbrolesets.rolesetid});
-- ALTER TABLE ${pms.table} ADD CONSTRAINT `fk_@{pms.table}_@{pms.pbrolesetid}' FOREIGN KEY (${pms.pbrolesetid})    REFERENCES ${pbroles.table}    (${pbroles.roleid});
-- ALTER TABLE ${pms.table} ADD CONSTRAINT `fk_@{pms.table}_@{pms.pbwordid}'    FOREIGN KEY (${pms.pbwordid})       REFERENCES ${pbwords.table}    (${pbwords.pbwordid});

-- ALTER TABLE ${pms.table} ADD CONSTRAINT `fk_@{pms.table}_@{pms.fnframeid}'   FOREIGN KEY (${pms.fnframeid})      REFERENCES ${fnframes.table}   (${fnframes.frameid});
-- ALTER TABLE ${pms.table} ADD CONSTRAINT `fk_@{pms.table}_@{pms.fnfeid}'      FOREIGN KEY (${pms.fnfeid})         REFERENCES ${fnfes.table}      (${fnfes.feid});
-- ALTER TABLE ${pms.table} ADD CONSTRAINT `fk_@{pms.table}_@{pms.fnfetypeid}'  FOREIGN KEY (${pms.fnfetypeid})     REFERENCES ${fnfetypes.table}  (${fnfetypes.fetypeid});
-- ALTER TABLE ${pms.table} ADD CONSTRAINT `fk_@{pms.table}_@{pms.fnluid}'      FOREIGN KEY (${pms.fnluid})         REFERENCES ${fnlexunits.table} (${fnlexunits.luid});
-- ALTER TABLE ${pms.table} ADD CONSTRAINT `fk_@{pms.table}_@{pms.fnwordid}'    FOREIGN KEY (${pms.fnwordid})       REFERENCES ${fnwords.table}    (${fnwords.fnwordid});
