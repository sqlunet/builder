#!/bin/bash
# 1313ou@gmail.com

db=sqlunet2
dbtag=2022

# C O L O R S

export R='\u001b[31m'
export G='\u001b[32m'
export B='\u001b[34m'
export Y='\u001b[33m'
export M='\u001b[35m'
export C='\u001b[36m'
export Z='\u001b[0m'

# C R E D E N T I A L S

source define_user.sh
 
# D I R S

thisdir=$(dirname $(readlink -m $0))
parentdir=$(readlink -m "${thisdir}/..")
outdir=${parentdir}/html
mkdir -p "${parentdir}"

# O U T P U T

outfile="${outdir}/${db}-${dbtag}-stats.html"

# O U T P U T

tables="words casedwords lexes senses synsets relations semrelations lexrelations poses domains morphs pronunciations samples vframes vtemplates adjpositions lexes_morphs lexes_pronunciations senses_vframes senses_vtemplates senses_adjpositions"

# M A I N

echo "<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'><title>${db}</title></head><body>" > ${outfile}
echo "<h1>Database ${db}</h1>" >> ${outfile}

# cardinality
echo "<h2>${db}-${dbtag}</h2>" >> ${outfile}
for t in ${tables}; do
mysql ${creds} ${db} --html <<EOF
SELECT "${t}" AS ${t}_table, COUNT(*) AS row_count FROM ${t};
EOF
done >> ${outfile}

# POS distribution
echo "<h1>POS</h1>" >> ${outfile}
echo "<h2>words</h2>" >> ${outfile}
mysql ${creds} ${db} --html >> ${outfile} <<EOF
SELECT posid, COUNT(DISTINCT word) AS words_count FROM words JOIN senses USING (wordid) JOIN synsets USING (synsetid) GROUP BY posid;
EOF
echo "<h2>senses</h2>" >> ${outfile}
mysql ${creds} ${db} --html >> ${outfile} <<EOF
SELECT posid, COUNT(*) AS senses_count FROM senses JOIN synsets USING (synsetid) GROUP BY posid;
EOF
echo "<h2>synsets</h2>" >> ${outfile}
mysql ${creds} ${db} --html >> ${outfile} <<EOF
SELECT posid, COUNT(*) AS synsets_count FROM synsets GROUP BY posid;
EOF

# relation distribution
echo "<h1>Relations</h1>" >> ${outfile}
echo "<h2>Semantic</h2>" >> ${outfile}
mysql ${creds} ${db} --html >> ${outfile} <<EOF
SELECT relationid, relation, s1.posid, s2.posid, COUNT(*) 
FROM semrelations AS l
INNER JOIN synsets AS s1 ON l.synset1id = s1.synsetid 
INNER JOIN synsets AS s2 ON l.synset2id = s2.synsetid 
INNER JOIN relations USING (relationid)
GROUP BY relationid, s1.posid, s2.posid
ORDER BY relationid, s1.posid, s2.posid;
EOF
echo "<h2>Lexical</h2>" >> ${outfile}
mysql ${creds} ${db} --html >> ${outfile} <<EOF
SELECT relationid, relation, s1.posid, s2.posid, COUNT(*) 
FROM lexrelations AS l
INNER JOIN words AS w1 ON l.word1id = w1.wordid 
INNER JOIN words AS w2 ON l.word2id = w2.wordid 
INNER JOIN synsets AS s1 ON l.synset1id = s1.synsetid 
INNER JOIN synsets AS s2 ON l.synset2id = s2.synsetid 
INNER JOIN relations USING (relationid)
GROUP BY relationid, s1.posid, s2.posid
ORDER BY relationid, s1.posid, s2.posid;
EOF

echo "</body></html>" >> ${outfile}

echo -e "${G}${outfile}${Z}" 

