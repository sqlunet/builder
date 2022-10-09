CREATE OR REPLACE VIEW `su_formulas_arg0s` AS SELECT ${formulas.formulaid},${terms.termid} FROM ${formulas_args.table} WHERE ${formulas_args.argtype} = 'a' AND ${formulas_args.argnum} = 0;
CREATE OR REPLACE VIEW `su_formulas_arg1s` AS SELECT ${formulas.formulaid},${terms.termid} FROM ${formulas_args.table} WHERE ${formulas_args.argtype} = 'a' AND ${formulas_args.argnum} = 1;
CREATE OR REPLACE VIEW `su_formulas_arg2s` AS SELECT ${formulas.formulaid},${terms.termid} FROM ${formulas_args.table} WHERE ${formulas_args.argtype} = 'a' AND ${formulas_args.argnum} = 2;
CREATE OR REPLACE VIEW `su_formulas_arg3s` AS SELECT ${formulas.formulaid},${terms.termid} FROM ${formulas_args.table} WHERE ${formulas_args.argtype} = 'a' AND ${formulas_args.argnum} = 3;

CREATE OR REPLACE VIEW `su_disjointformulas` AS SELECT ${formulas.formulaid} FROM `su_formulas_arg0s` INNER JOIN ${terms.table} USING (${terms.termid}) WHERE ${terms.term} = 'disjoint';
CREATE OR REPLACE VIEW `su_domainformulas` AS SELECT ${formulas.formulaid} FROM `su_formulas_arg0s` INNER JOIN ${terms.table} USING (${terms.termid}) WHERE ${terms.term} = 'domain';
CREATE OR REPLACE VIEW `su_instanceformulas` AS SELECT ${formulas.formulaid} FROM `su_formulas_arg0s` INNER JOIN ${terms.table} USING (${terms.termid}) WHERE ${terms.term} = 'instance';
CREATE OR REPLACE VIEW `su_partitionformulas` AS SELECT ${formulas.formulaid} FROM `su_formulas_arg0s` INNER JOIN ${terms.table} USING (${terms.termid}) WHERE ${terms.term} = 'partition';
CREATE OR REPLACE VIEW `su_subclassformulas` AS SELECT ${formulas.formulaid} FROM `su_formulas_arg0s` INNER JOIN ${terms.table} USING (${terms.termid}) WHERE ${terms.term} = 'subclass';
CREATE OR REPLACE VIEW `su_subrelationformulas` AS SELECT ${formulas.formulaid} FROM `su_formulas_arg0s` INNER JOIN ${terms.table} USING (${terms.termid}) WHERE ${terms.term} = 'subrelation';

CREATE OR REPLACE VIEW `su_disjoints` AS SELECT a1.${terms.termid} AS `disjoint1id`, a2.${terms.termid} AS `disjoint2id`,${formulas.formulaid} FROM `su_disjointformulas` AS a0 INNER JOIN `su_formulas_arg1s` AS a1 USING (${formulas.formulaid}) INNER JOIN `su_formulas_arg2s` AS a2 USING (${formulas.formulaid});
CREATE OR REPLACE VIEW `su_instances` AS SELECT a1.${terms.termid} AS `instanceid`, a2.${terms.termid} AS `classid`,${formulas.formulaid} FROM `su_instanceformulas` AS a0 INNER JOIN `su_formulas_arg1s` AS a1 USING (${formulas.formulaid}) INNER JOIN `su_formulas_arg2s` AS a2 USING (${formulas.formulaid});
CREATE OR REPLACE VIEW `su_subclasses` AS SELECT a1.${terms.termid} AS `classid`, a2.${terms.termid} AS `superclassid`,${formulas.formulaid} FROM `su_subclassformulas` AS a0 INNER JOIN `su_formulas_arg1s` AS a1 USING (${formulas.formulaid}) INNER JOIN `su_formulas_arg2s` AS a2 USING (${formulas.formulaid});
CREATE OR REPLACE VIEW `su_subrelations` AS SELECT a1.${terms.termid} AS `relationid`, a2.${terms.termid} AS `superrelationid`,${formulas.formulaid} FROM `su_subrelationformulas` AS a0 INNER JOIN `su_formulas_arg1s` AS a1 USING (${formulas.formulaid}) INNER JOIN `su_formulas_arg2s` AS a2 USING (${formulas.formulaid});
CREATE OR REPLACE VIEW `su_relations` AS SELECT ${terms.termid} FROM `su_formulas_arg0s` INNER JOIN ${terms.table} USING (${terms.termid}) GROUP BY ${terms.termid};
CREATE OR REPLACE VIEW `su_rules` AS SELECT ${formulas.formulaid},${formulas.formula},${formulas.fileid} FROM ${formulas.table} INNER JOIN ${formulas_args.table} USING (${formulas.formulaid}) INNER JOIN ${terms.table} USING (${terms.termid}) WHERE ${formulas_args.argnum} = 0 AND (${terms.term}='=>' OR ${terms.term} = '<=>');

CREATE OR REPLACE VIEW `su_taggedterms` AS SELECT ${terms.termid},${terms.term},GROUP_CONCAT(${terms_attrs.attr} ORDER BY ${terms_attrs.attr} SEPARATOR '') AS `attrtag` FROM ${terms.table} INNER JOIN ${terms_attrs.table} USING (${terms.termid}) GROUP BY ${terms.termid};
