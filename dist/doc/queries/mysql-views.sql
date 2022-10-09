CREATE OR REPLACE VIEW `samplesets` AS SELECT `synsetid`,GROUP_CONCAT(DISTINCT `sample` ORDER BY `sampleid` SEPARATOR '|') AS `sampleset` FROM `samples` GROUP BY `synsetid`;
CREATE OR REPLACE VIEW `dict` AS SELECT * FROM `words` w LEFT JOIN `senses` s USING (`wordid`) LEFT JOIN `casedwords` c USING (`wordid`,`casedwordid`) LEFT JOIN `synsets` y USING (`synsetid`) LEFT JOIN `samplesets` USING (`synsetid`);

CREATE OR REPLACE VIEW `vn_examplesets` AS SELECT `frameid`,GROUP_CONCAT(`example` ORDER BY `exampleid` SEPARATOR '|') AS `exampleset` FROM `vn_frames_examples` INNER JOIN `vn_examples` USING (`exampleid`) GROUP BY `frameid`;

CREATE OR REPLACE VIEW `fn_patterns` AS SELECT * FROM `fn_ferealizations_valenceunits`;

CREATE OR REPLACE VIEW `su_formulas_arg0s` AS SELECT `formulaid`,`sumoid` FROM `su_formulas_args` WHERE `argtype` = 'a' AND `argnum` = 0;
CREATE OR REPLACE VIEW `su_formulas_arg1s` AS SELECT `formulaid`,`sumoid` FROM `su_formulas_args` WHERE `argtype` = 'a' AND `argnum` = 1;
CREATE OR REPLACE VIEW `su_formulas_arg2s` AS SELECT `formulaid`,`sumoid` FROM `su_formulas_args` WHERE `argtype` = 'a' AND `argnum` = 2;
CREATE OR REPLACE VIEW `su_formulas_arg3s` AS SELECT `formulaid`,`sumoid` FROM `su_formulas_args` WHERE `argtype` = 'a' AND `argnum` = 3;
CREATE OR REPLACE VIEW `su_disjointformulas` AS SELECT `formulaid` FROM `su_formulas_arg0s` INNER JOIN `su_terms` USING (`sumoid`) WHERE `term` = 'disjoint';
CREATE OR REPLACE VIEW `su_domainformulas` AS SELECT `formulaid` FROM `su_formulas_arg0s` INNER JOIN `su_terms` USING (`sumoid`) WHERE `term` = 'domain';
CREATE OR REPLACE VIEW `su_instanceformulas` AS SELECT `formulaid` FROM `su_formulas_arg0s` INNER JOIN `su_terms` USING (`sumoid`) WHERE `term` = 'instance';
CREATE OR REPLACE VIEW `su_partitionformulas` AS SELECT `formulaid` FROM `su_formulas_arg0s` INNER JOIN `su_terms` USING (`sumoid`) WHERE `term` = 'partition';
CREATE OR REPLACE VIEW `su_subclassformulas` AS SELECT `formulaid` FROM `su_formulas_arg0s` INNER JOIN `su_terms` USING (`sumoid`) WHERE `term` = 'subclass';
CREATE OR REPLACE VIEW `su_subrelationformulas` AS SELECT `formulaid` FROM `su_formulas_arg0s` INNER JOIN `su_terms` USING (`sumoid`) WHERE `term` = 'subrelation';
CREATE OR REPLACE VIEW `su_disjoints` AS SELECT a1.`sumoid` AS `disjoint1id`, a2.`sumoid` AS `disjoint2id`,`formulaid` FROM `su_disjointformulas` AS a0 INNER JOIN `su_formulas_arg1s` AS a1 USING (`formulaid`) INNER JOIN `su_formulas_arg2s` AS a2 USING (`formulaid`);
CREATE OR REPLACE VIEW `su_instances` AS SELECT a1.`sumoid` AS `instanceid`, a2.`sumoid` AS `classid`,`formulaid` FROM `su_instanceformulas` AS a0 INNER JOIN `su_formulas_arg1s` AS a1 USING (`formulaid`) INNER JOIN `su_formulas_arg2s` AS a2 USING (`formulaid`);
CREATE OR REPLACE VIEW `su_subclasses` AS SELECT a1.`sumoid` AS `classid`, a2.`sumoid` AS `superclassid`,`formulaid` FROM `su_subclassformulas` AS a0 INNER JOIN `su_formulas_arg1s` AS a1 USING (`formulaid`) INNER JOIN `su_formulas_arg2s` AS a2 USING (`formulaid`);
CREATE OR REPLACE VIEW `su_subrelations` AS SELECT a1.`sumoid` AS `relationid`, a2.`sumoid` AS `superrelationid`,`formulaid` FROM `su_subrelationformulas` AS a0 INNER JOIN `su_formulas_arg1s` AS a1 USING (`formulaid`) INNER JOIN `su_formulas_arg2s` AS a2 USING (`formulaid`);
CREATE OR REPLACE VIEW `su_relations` AS SELECT `sumoid` FROM `su_formulas_arg0s` INNER JOIN `su_terms` USING (`sumoid`) GROUP BY `sumoid`;
CREATE OR REPLACE VIEW `su_rules` AS SELECT `formulaid`,`formula`,`fileid` FROM `su_formulas` INNER JOIN `su_formulas_args` USING (`formulaid`) INNER JOIN `su_terms` USING (`sumoid`) WHERE `argnum` = 0 AND (`term`='=>' OR `term` = '<=>');
CREATE OR REPLACE VIEW `su_taggedterms` AS SELECT `sumoid`,`term`,GROUP_CONCAT(`attr` ORDER BY `attr` SEPARATOR '') AS `attrtag` FROM `su_terms` INNER JOIN `su_terms_attrs` USING (`sumoid`) GROUP BY `sumoid`;
