ALTER TABLE ${pms.table} ADD CONSTRAINT `fk_@{pms.table}_@{pms.pmroleid}`    FOREIGN KEY (${pms.pmroleid})            REFERENCES ${roles.table}      (${roles.roleid});

-- ALTER TABLE ${pms.table} ADD CONSTRAINT `fk_@{pms.table}_@{pms.wordid}`      FOREIGN KEY (${pms.wordid})           REFERENCES ${wnwords.table}    (${wnwords.wordid});
-- ALTER TABLE ${pms.table} ADD CONSTRAINT `fk_@{pms.table}_@{pms.synsetid}`    FOREIGN KEY (${pms.synsetid})         REFERENCES ${wnsynsets.table}  (${wnsynsets.synsetid});

-- ALTER TABLE ${pms.table} ADD CONSTRAINT `fk_@{pms.table}_@{pms.vn_classid}`   FOREIGN KEY (${pms.vn_classid})      REFERENCES ${vnclasses.table}  (${vnclasses.vnclassid});
-- ALTER TABLE ${pms.table} ADD CONSTRAINT `fk_@{pms.table}_@{pms.vn_roleid}`    FOREIGN KEY (${pms.vn_roleid})       REFERENCES ${vnroles.table}    (${vnroles.vnroleid});
-- ALTER TABLE ${pms.table} ADD CONSTRAINT `fk_@{pms.table}_@{pms.vn_wordid}`    FOREIGN KEY (${pms.vn_wordid})       REFERENCES ${vnwords.table}    (${vnwords.vnwordid});

-- ALTER TABLE ${pms.table} ADD CONSTRAINT `fk_@{pms.table}_@{pms.pb_rolesetid}` FOREIGN KEY (${pms.pb_rolesetid})    REFERENCES ${pbrolesets.table} (${pbrolesets.pbrolesetid});
-- ALTER TABLE ${pms.table} ADD CONSTRAINT `fk_@{pms.table}_@{pms.pb_roleid}`    FOREIGN KEY (${pms.pb_roleid})       REFERENCES ${pbroles.table}    (${pbroles.pbroleid});
-- ALTER TABLE ${pms.table} ADD CONSTRAINT `fk_@{pms.table}_@{pms.pb_wordid}`    FOREIGN KEY (${pms.pb_wordid})       REFERENCES ${pbwords.table}    (${pbwords.pbwordid});

-- ALTER TABLE ${pms.table} ADD CONSTRAINT `fk_@{pms.table}_@{pms.fn_frameid}`   FOREIGN KEY (${pms.fn_frameid})      REFERENCES ${fnframes.table}   (${fnframes.fnframeid});
-- ALTER TABLE ${pms.table} ADD CONSTRAINT `fk_@{pms.table}_@{pms.fn_feid}`      FOREIGN KEY (${pms.fn_feid})         REFERENCES ${fnfes.table}      (${fnfes.fnfeid});
-- ALTER TABLE ${pms.table} ADD CONSTRAINT `fk_@{pms.table}_@{pms.fn_fetypeid}`  FOREIGN KEY (${pms.fn_fetypeid})     REFERENCES ${fnfetypes.table}  (${fnfetypes.fnfetypeid});
-- ALTER TABLE ${pms.table} ADD CONSTRAINT `fk_@{pms.table}_@{pms.fn_luid}`      FOREIGN KEY (${pms.fn_luid})         REFERENCES ${fnlexunits.table} (${fnlexunits.fnluid});
-- ALTER TABLE ${pms.table} ADD CONSTRAINT `fk_@{pms.table}_@{pms.fn_wordid}`    FOREIGN KEY (${pms.fn_wordid})       REFERENCES ${fnwords.table}    (${fnwords.fnwordid});
