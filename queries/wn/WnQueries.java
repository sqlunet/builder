package provider;

public class WnQueries {

static public class WORDS {
	static public final String TABLE = "${words.table}";
}

static public class WORD {
	static public final String TABLE = "${words.table}";
}

static public class SENSES {
	static public final String TABLE = "${senses.table}";
}

static public class SENSE {
	static public final String TABLE = "${senses.table}";
}

static public class SYNSETS {
	static public final String TABLE = "${synsets.table}";
}

static public class SYNSET {
	static public final String TABLE = "${synsets.table}";
}

static public class SEMRELATIONS {
	static public final String TABLE = "synsets_synsets";
}

static public class LEXRELATIONS {
	static public final String TABLE = "${lexrelations.table}";
}

static public class RELATIONS {
	static public final String TABLE = "${relations.table}";
}

static public class POSES {
	static public final String TABLE = "${poses.table}";
}

static public class DOMAINS {
	static public final String TABLE = "${domains.table}";
}

static public class ADJPOSITIONS {
	static public final String TABLE = "${adjpositions.table}";
}

static public class SAMPLES {
	static public final String TABLE = "${samples.table}";
}

static public class DICT {
	static public final String TABLE = "URI_PATH_SEGMENT";
}

static public class WORDS_SENSES_SYNSETS {
	static public final String TABLE = "${words.table} AS w LEFT JOIN ${senses.table} AS s USING (${senses.wordid}) LEFT JOIN ${synsets.table} AS y USING (${synsets.synsetid})";
}

static public class WORDS_SENSES_CASEDWORDS_SYNSETS {
	static public final String TABLE = "${words.table} AS w LEFT JOIN ${senses.table} AS s USING (${senses.wordid}) LEFT JOIN ${casedwords.table} AS c USING (${casedwords.wordid},${casedwords.casedwordid}) LEFT JOIN ${synsets.table} AS y USING (${synsets.synsetid})";
}

static public class WORDS_SENSES_CASEDWORDS_SYNSETS_POSTYPES_LEXDOMAINS {
	static public final String TABLE = "${words.table} AS w LEFT JOIN ${senses.table} AS s USING (${senses.wordid}) LEFT JOIN ${casedwords.table} AS c USING (${casedwords.wordid},${casedwords.casedwordid}) LEFT JOIN ${synsets.table} AS y USING (${synsets.synsetid}) LEFT JOIN ${poses.table} AS p USING (${poses.posid}) LEFT JOIN ${domains.table} AS m USING (${domains.domainid})";
}

static public class SENSES_WORDS {
	static public final String TABLE = "${senses.table} AS s LEFT JOIN ${words.table} AS w USING (${senses.wordid})";
}

static public class SENSES_WORDS_BY_SYNSET {
	static public final String TABLE = "${senses.table} AS s LEFT JOIN ${words.table} AS w USING (${words.wordid})";
	static public final String[] PROJECTION = {"*","GROUP_CONCAT(${words.table}.${words.word}, ', ' ) AS ${senses_words.members}"};
	static public final String GROUPBY = "${synsets.synsetid}";
}

static public class SENSES_SYNSETS_POSES_DOMAINS {
	static public final String TABLE = "${senses.table} AS s INNER JOIN ${synsets.table} AS y USING (${synsets.synsetid}) LEFT JOIN ${poses.table} AS p USING (${poses.posid}) LEFT JOIN ${domains.table} AS m USING (${domains.domainid})";
}

static public class SYNSETS_POSES_DOMAINS {
	static public final String TABLE = "${synsets.table} AS y LEFT JOIN ${poses.table} AS p USING (${poses.posid}) LEFT JOIN ${domains.table} AS m USING (${domains.domainid})";
}

static public class BASERELATIONS_SENSES_WORDS_X_BY_SYNSET {
	static public final String TABLE = "( MAKEQUERY ) AS r INNER JOIN ${relations.table} USING (${relations.relationid}) INNER JOIN ${synsets.table} AS d ON r.${baserelations.synsetid2} = d.${synsets.synsetid} LEFT JOIN ${senses.table} ON d.${synsets.synsetid} = ${senses.table}.${senses.synsetid} LEFT JOIN ${words.table} AS w USING (${words.wordid}) LEFT JOIN ${words.table} AS t ON r.${baserelations.wordid2} = t.${words.wordid}";
	static public final String GROUPBY = "${d_synsetid},x,${relations.relation},${relations.relationid},${d_wordid},${d_word}";
}

static public class SEMRELATIONS_SYNSETS {
	static public final String TABLE = "${semrelations.table} AS r INNER JOIN ${synsets.table} AS d ON r.${semrelations.synsetid2} = d.${synsets.synsetid}";
}

static public class SEMRELATIONS_SYNSETS_X {
	static public final String TABLE = "${semrelations.table} AS r INNER JOIN ${relations.table} USING (${relations.relationid})INNER JOIN ${synsets.table} AS d ON r.${semrelations.synsetid2} = d.${synsets.synsetid} ";
}

static public class SEMRELATIONS_SYNSETS_WORDS_X_BY_SYNSET {
	static public final String TABLE = "${semrelations.table} AS r INNER JOIN ${relations.table} USING (${relations.relationid}) INNER JOIN ${synsets.table} AS d ON r.${semrelations.synsetid2} = d.${synsets.synsetid} LEFT JOIN ${senses.table} ON d.${synsets.synsetid} = ${senses.table}.${senses.synsetid} LEFT JOIN ${words.table} USING (${words.wordid})";
	static public final String[] PROJECTION = {"*","GROUP_CONCAT(${words.table}.${words.word}, ', ' ) AS ${semrelations_synsets_words_x.members2}"};
	static public final String GROUPBY = "d.${synsets.synsetid}";
}

static public class LEXRELATIONS_SENSES {
	static public final String TABLE = "${lexrelations.table} AS r INNER JOIN ${synsets.table} AS d ON r.${lexrelations.synsetid2} = d.${synsets.synsetid} INNER JOIN ${words.table} AS w ON r.${lexrelations.wordid2} = w.${words.wordid}";
}

static public class LEXRELATIONS_SENSES_X {
	static public final String TABLE = "${lexrelations.table} AS r INNER JOIN ${relations.table} USING (${relations.relationid})INNER JOIN ${synsets.table} AS d ON r.${lexrelations.synsetid2} = d.${synsets.synsetid} INNER JOIN ${words.table} AS w ON r.${lexrelations.wordid2} = w.${words.wordid} ";
}

static public class LEXRELATIONS_SENSES_WORDS_X_BY_SYNSET {
	static public final String TABLE = "${lexrelations.table} AS r INNER JOIN ${relations.table} USING (${relations.relationid}) INNER JOIN ${synsets.table} AS d ON r.${lexrelations.synsetid2} = d.${synsets.synsetid} INNER JOIN ${words.table} AS w ON r.${lexrelations.wordid2} = w.${words.wordid} LEFT JOIN ${senses.table} AS s ON d.${senses.synsetid} = s.${senses.synsetid} LEFT JOIN ${words.table} AS t USING (${words.wordid})";
	static public final String[] PROJECTION = {"*","GROUP_CONCAT(DISTINCT t.${words.word}) AS ${lexrelations_senses_words_x.members2}"};
	static public final String GROUPBY = "d.${synsets.synsetid}";
}

static public class SENSES_VFRAMES {
	static public final String TABLE = "${senses_verbframes.table} LEFT JOIN ${vframes.table} USING (${vframes.frameid})";
}

static public class SENSES_VTEMPLATES {
	static public final String TABLE = "${senses_verbtemplates.table} LEFT JOIN ${vtemplates.table} USING (${vtemplates.templateid})";
}

static public class SENSES_ADJPOSITIONS {
	static public final String TABLE = "${senses_adjpositions.table} LEFT JOIN ${adjpositions.table} USING (${adjpositions.positionid})";
}

static public class LEXES_MORPHS {
	static public final String TABLE = "${lexes_morphs.table} LEFT JOIN ${morphs.table} USING (${morphs.morphid})";
}

static public class WORDS_LEXES_MORPHS {
	static public final String TABLE = "${words.table} LEFT JOIN ${lexes_morphs.table} USING (${words.wordid}) LEFT JOIN ${morphs.table} USING (${morphs.morphid})";
}

static public class WORDS_LEXES_MORPHS_BY_WORD {
	static public final String TABLE = "${words.table} LEFT JOIN ${lexes_morphs.table} USING (${words.wordid}) LEFT JOIN ${morphs.table} USING (${morphs.morphid})";
	static public final String GROUPBY = "${words.wordid}";
}

static public class LOOKUP_FTS_WORDS {
	static public final String TABLE = "${words.table}_${words.word}_fts4";
}

static public class LOOKUP_FTS_DEFINITIONS {
	static public final String TABLE = "${synsets.table}_${synsets.definition}_fts4";
}

static public class LOOKUP_FTS_SAMPLES {
	static public final String TABLE = "${samples.table}_${samples.sample}_fts4";
}

static public class SUGGEST_WORDS {
	static public final String TABLE = "${words.table}";
	static public final String[] PROJECTION = {"${words.wordid} AS _id","${words.word} AS SearchManager.SUGGEST_COLUMN_TEXT_1","${words.word} AS SearchManager.SUGGEST_COLUMN_QUERY"};
	static public final String SELECTION = "${words.word} LIKE ? || '%'";
	static public final String[] ARGS = {"${uri_last}"};
}

static public class SUGGEST_FTS_WORDS {
	static public final String TABLE = "${words.table}_${words.word}_fts4";
	static public final String[] PROJECTION = {"${words.wordid} AS _id","${words.word} AS SearchManager.SUGGEST_COLUMN_TEXT_1","${words.word} AS SearchManager.SUGGEST_COLUMN_QUERY"};
	static public final String SELECTION = "${words.word} MATCH ?";
	static public final String[] ARGS = {"${uri_last}*"};
}

static public class SUGGEST_FTS_DEFINITIONS {
	static public final String TABLE = "${synsets.table}_${synsets.definition}_fts4";
	static public final String[] PROJECTION = {"${synsets.synsetid} AS _id","${synsets.definition} AS SearchManager.SUGGEST_COLUMN_TEXT_1","${synsets.definition} AS SearchManager.SUGGEST_COLUMN_QUERY"};
	static public final String SELECTION = "${synsets.definition} MATCH ?";
	static public final String[] ARGS = {"${uri_last}*"};
}

static public class SUGGEST_FTS_SAMPLES {
	static public final String TABLE = "${samples.table}_${samples.sample}_fts4";
	static public final String[] PROJECTION = {"${samples.sampleid} AS _id","${samples.sample} AS SearchManager.SUGGEST_COLUMN_TEXT_1","${samples.sample} AS SearchManager.SUGGEST_COLUMN_QUERY"};
	static public final String SELECTION = "${samples.sample} MATCH ?";
	static public final String[] ARGS = {"${uri_last}*"};
}

}
