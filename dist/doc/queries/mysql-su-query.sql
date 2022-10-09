SELECT 'SUMO' AS section;

CREATE OR REPLACE VIEW su_formulas_arg0s AS
SELECT formulaid, sumoid
FROM su_formulas_args
WHERE argtype = 'a' AND argnum = 0;

CREATE OR REPLACE VIEW su_relations AS 
SELECT sumoid 
FROM su_formulas_arg0s 
JOIN su_terms USING (sumoid)
GROUP BY sumoid;

CREATE OR REPLACE VIEW su_taggedterms AS
SELECT sumoid,term,GROUP_CONCAT(attr ORDER BY attr SEPARATOR '') AS attrtag 
FROM su_terms 
INNER JOIN su_terms_attrs USING (sumoid)
GROUP BY sumoid;

SELECT 'terms' AS subsection;

SELECT 'terms' AS comment;
SELECT * FROM su_terms
WHERE term LIKE 'R%'
ORDER BY sumoid
LIMIT 26;

SELECT 'functors' AS comment;
SELECT * 
FROM su_terms
WHERE term LIKE '%Fn'
LIMIT 20;

SELECT 'term Entity' AS comment;
SELECT * FROM su_terms
WHERE term = "Entity";

SELECT 'term Class (flat version)' AS comment;
SELECT * FROM su_terms
WHERE term = "Class";

SELECT 'attributes' AS subsection;

SELECT 'term attributes' AS comment;
SELECT attr FROM su_terms_attrs
GROUP BY attr
ORDER BY attr;

SELECT DISTINCT attr FROM su_terms_attrs
ORDER BY attr;

SELECT 'attributes occurrences' AS comment;
SELECT attr, COUNT(*) FROM su_terms_attrs GROUP BY attr;

SELECT 'co-occurring attributes' AS comment;
SELECT DISTINCT t1.attr AS attr,t2.attr AS cooccurringattr 
FROM su_terms_attrs AS t1 
INNER JOIN su_terms_attrs AS t2 USING (sumoid)
WHERE t2.attr <> t1.attr;

SELECT 'attribute tags' AS comment;
SELECT attrtag FROM su_taggedterms
GROUP BY attrtag
ORDER BY attrtag;

SELECT 'types of terms' AS subsection;

SELECT 'math functions' AS comment;
SELECT * 
FROM su_terms
LEFT JOIN su_terms_attrs USING (sumoid)
WHERE attr = 'm';

SELECT 'logical functions' AS comment;
SELECT * 
FROM su_terms
LEFT JOIN su_terms_attrs USING (sumoid)
WHERE attr = 'l';

SELECT 'quantifiers' AS comment;
SELECT * 
FROM su_terms
LEFT JOIN su_terms_attrs USING (sumoid)
WHERE attr = 'q';

SELECT 'comparison operators' AS comment;
SELECT * 
FROM su_terms
LEFT JOIN su_terms_attrs USING (sumoid)
WHERE attr = '~';

SELECT 'functions' AS comment;
SELECT * 
FROM su_terms
LEFT JOIN su_terms_attrs USING (sumoid)
WHERE attr = 'y'
LIMIT 20;

SELECT 'relation, attribute, predicate, function' AS comment;
SELECT * 
FROM su_terms
LEFT JOIN su_terms_attrs USING (sumoid)
WHERE attr IN ('r','a','p','f') AND term LIKE 'a%' OR term LIKE 'A%' ;

SELECT 'relations' AS subsection;

SELECT 'syntactic relations' AS comment;
SELECT *
FROM su_relations
INNER JOIN su_terms USING (sumoid)
LIMIT 30;

SELECT 'Relation term in formulas' AS comment;
SELECT formula,SUBSTRING(formula FROM 1 FOR 64) AS formula 
FROM su_formulas
INNER JOIN su_formulas_args AS p1 USING (formulaid)
INNER JOIN su_formulas_args AS p2 USING (formulaid)
INNER JOIN su_terms AS t ON t.sumoid = p2.sumoid
WHERE term = 'Relation' AND
p1.argtype = 'a' AND p1.argnum =0 AND p2.argtype = 'a' AND p2.argnum > 0
ORDER BY formula;

SELECT 'formulas' AS subsection;

SELECT 'subclass formulas' AS comment;
SELECT *
FROM su_subclasses
INNER JOIN su_formulas USING(formulaid)
LIMIT 20;

SELECT 'instance formulas' AS comment;
SELECT * FROM su_instances
INNER JOIN su_formulas USING(formulaid)
LIMIT 20;

SELECT 'formulas having term "Bus"' AS comment;
SELECT * FROM su_formulas
INNER JOIN su_formulas_args USING (formulaid)
INNER JOIN su_terms USING (sumoid)
WHERE term='Bus';

SELECT 'formulas (subclass X Vehicle)' AS comment;
SELECT formula FROM su_subclasses AS s
INNER JOIN su_terms t ON t.sumoid = s.superclassid
INNER JOIN su_formulas USING(formulaid)
WHERE term='Vehicle';

SELECT 'formulas (subclass Vehicle X)' AS comment;
SELECT formula FROM su_subclasses AS c
INNER JOIN su_terms t ON t.sumoid = c.classid
INNER JOIN su_formulas USING(formulaid)
WHERE term='Vehicle';

SELECT 'formulas (instance Paris X)' AS comment;
SELECT formula FROM su_instances AS i
INNER JOIN su_terms t ON t.sumoid = i.instanceid
INNER JOIN su_formulas USING(formulaid)
WHERE term='Paris';

SELECT 'formulas (instance X EuropeanNation)' AS comment;
SELECT formula FROM su_instances AS i
INNER JOIN su_terms t ON t.sumoid = i.classid
INNER JOIN su_formulas USING(formulaid)
WHERE term='EuropeanNation';

SELECT 'formulas (subrelation sister X)' AS comment;
SELECT formula FROM su_subrelations AS r
INNER JOIN su_terms t ON t.sumoid = r.relationid
INNER JOIN su_formulas USING(formulaid)
WHERE term='sister';

SELECT 'formulas (subrelation X sister)' AS comment;
SELECT formula FROM su_subrelations AS r
INNER JOIN su_terms t ON t.sumoid = r.superrelationid
INNER JOIN su_formulas USING(formulaid)
WHERE term='sibling';

SELECT 'rules' AS subsection;

SELECT 'rules' AS comment;
SELECT *
FROM su_rules
LIMIT 20;

SELECT 'rules with "Vehicle" in premise' AS comment;
SELECT *
FROM su_rules
INNER JOIN su_formulas_args USING (formulaid)
INNER JOIN su_terms USING (sumoid)
WHERE term  = 'Vehicle' and argtype = 'p';

SELECT 'rules with "Vehicle" in conclusion' AS comment;
SELECT *
FROM su_rules
INNER JOIN su_formulas_args USING (formulaid)
INNER JOIN su_terms USING (sumoid)
WHERE term  = 'Vehicle' and argtype = 'c';

SELECT 'term tables from formulas' AS subsection;

SELECT 'subclass-superclass' AS comment;
SELECT t1.term AS class,t2.term AS superclass,formula
FROM su_subclasses AS r
INNER JOIN su_formulas USING (formulaid) 
INNER JOIN su_terms AS t1 ON r.classid = t1.sumoid
INNER JOIN su_terms AS t2 ON r.superclassid = t2.sumoid
ORDER BY class
LIMIT 20;

SELECT 'relation-superrelation' AS comment;
SELECT t1.term AS relation,t2.term AS superrelation,formula
FROM su_subrelations AS r
INNER JOIN su_formulas USING (formulaid) 
INNER JOIN su_terms AS t1 ON r.relationid = t1.sumoid
INNER JOIN su_terms AS t2 ON r.superrelationid = t2.sumoid
ORDER BY relation
LIMIT 20;

SELECT 'instance-class' AS comment;
SELECT t1.term AS instance,t2.term AS class,formula
FROM su_instances AS r
INNER JOIN su_formulas USING (formulaid) 
INNER JOIN su_terms AS t1 ON r.instanceid = t1.sumoid
INNER JOIN su_terms AS t2 ON r.classid = t2.sumoid
ORDER BY instance
LIMIT 20;

SELECT 'disjoints' AS comment;
SELECT t1.term AS disjoint1,t2.term AS disjoint2,formula
FROM su_disjoints AS r
INNER JOIN su_formulas USING (formulaid) 
INNER JOIN su_terms AS t1 ON r.disjoint1id = t1.sumoid
INNER JOIN su_terms AS t2 ON r.disjoint2id = t2.sumoid
ORDER BY disjoint1
LIMIT 20;

SELECT 'classes that are subclass of Vehicle' AS comment;
SELECT t1.term AS class
FROM su_subclasses AS r
INNER JOIN su_formulas USING (formulaid) 
INNER JOIN su_terms AS t1 ON r.classid = t1.sumoid
INNER JOIN su_terms AS t2 ON r.superclassid = t2.sumoid
WHERE t2.term = 'Vehicle';

SELECT 'class(es) that Vehicle is a subclass of' AS comment;
SELECT t2.term AS class
FROM su_subclasses AS r
INNER JOIN su_formulas USING (formulaid) 
INNER JOIN su_terms AS t1 ON r.classid = t1.sumoid
INNER JOIN su_terms AS t2 ON r.superclassid = t2.sumoid
WHERE t1.term = 'Vehicle';

SELECT 'wordnet map' AS subsection;

SELECT 'types of sumo-wn relations' AS comment;
SELECT maptype,COUNT(*) AS count FROM su_terms_synsets
GROUP BY maptype;

SELECT 'mapped sumo terms' AS comment; 
SELECT word,synsetid,SUBSTRING(definition FROM 1 FOR 32),maptype,term 
FROM su_terms_synsets
INNER JOIN synsets USING (synsetid)
INNER JOIN senses USING (synsetid)
INNER JOIN words USING (wordid)
INNER JOIN su_terms USING (sumoid)
WHERE word > 'ab' AND word < 'ac'
LIMIT 20;

SELECT 'formula involving WordNet "car"' AS comment; 
SELECT word,synsetid,SUBSTRING(definition FROM  1 FOR 32),maptype,term, formula
FROM su_terms_synsets
INNER JOIN su_formulas_args USING(sumoid)
INNER JOIN su_formulas USING(formulaid)
INNER JOIN synsets USING (synsetid)
INNER JOIN senses USING (synsetid)
INNER JOIN words USING (wordid)
INNER JOIN su_terms USING (sumoid)
WHERE word = 'car' AND maptype = '='
LIMIT 20;

SELECT 'statistics' AS subsection;

SELECT attr,COUNT(*)AS count FROM su_terms_attrs GROUP BY attr ORDER BY attr;
SELECT attrtag,COUNT(*) AS count FROM su_taggedterms GROUP BY attrtag ORDER BY attrtag;
SELECT COUNT(*) AS countofterms FROM su_terms;
SELECT COUNT(*) AS countofrules FROM su_rules;
SELECT COUNT(*) AS countofformulas FROM su_formulas;
SELECT COUNT(*) AS countoffunctionterms FROM su_terms WHERE term LIKE '%Fn';
SELECT COUNT(*) AS countofsubclassformulas FROM su_subclassformulas;
SELECT COUNT(*) AS countofsubrelationformulas FROM su_subrelationformulas;
SELECT COUNT(*) AS countofinstanceformulas FROM su_instanceformulas;
SELECT COUNT(*) AS countofdisjointformulas FROM su_disjointformulas;
