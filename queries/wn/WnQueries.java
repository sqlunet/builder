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
	static public final String TABLE = "${synsets_synsets.table}";
}

static public class LEXRELATIONS {
	static public final String TABLE = "${senses_senses.table}";
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
	static public final String TABLE = "${words.table} AS ${as_word} LEFT JOIN ${senses.table} AS ${as_sense} USING (${senses.wordid}) LEFT JOIN ${synsets.table} AS ${as_synset} USING (${synsets.synsetid})";
}

static public class WORDS_SENSES_CASEDWORDS_SYNSETS {
	static public final String TABLE = "${words.table} AS ${as_word} LEFT JOIN ${senses.table} AS ${as_sense} USING (${senses.wordid}) LEFT JOIN ${casedwords.table} AS ${as_cased} USING (${casedwords.wordid},${casedwords.casedwordid}) LEFT JOIN ${synsets.table} AS ${as_synset} USING (${synsets.synsetid})";
}

static public class WORDS_SENSES_CASEDWORDS_SYNSETS_POSTYPES_LEXDOMAINS {
	static public final String TABLE = "${words.table} AS ${as_word} LEFT JOIN ${senses.table} AS ${as_sense} USING (${senses.wordid}) LEFT JOIN ${casedwords.table} AS ${as_cased} USING (${casedwords.wordid},${casedwords.casedwordid}) LEFT JOIN ${synsets.table} AS ${as_synset} USING (${synsets.synsetid}) LEFT JOIN ${poses.table} AS ${as_pos} USING (${poses.posid}) LEFT JOIN ${domains.table} AS ${as_domain} USING (${domains.domainid})";
}

static public class SENSES_WORDS {
	static public final String TABLE = "${senses.table} AS ${as_sense} LEFT JOIN ${words.table} AS ${as_word} USING (${senses.wordid})";
}

static public class SENSES_WORDS_BY_SYNSET {
	static public final String TABLE = "${senses.table} AS ${as_sense} LEFT JOIN ${words.table} AS ${as_word} USING (${words.wordid})";
	static public final String[] PROJECTION = {"*","GROUP_CONCAT(${words.table}.${words.word}, ', ' ) AS ${senses_words.members}"};
	static public final String GROUPBY = "${synsets.synsetid}";
}

static public class SENSES_SYNSETS_POSES_DOMAINS {
	static public final String TABLE = "${senses.table} AS ${as_sense} INNER JOIN ${synsets.table} AS ${as_synset} USING (${synsets.synsetid}) LEFT JOIN ${poses.table} AS ${as_pos} USING (${poses.posid}) LEFT JOIN ${domains.table} AS ${as_domain} USING (${domains.domainid})";
}

static public class SYNSETS_POSES_DOMAINS {
	static public final String TABLE = "${synsets.table} AS ${as_synset} LEFT JOIN ${poses.table} AS ${as_pos} USING (${poses.posid}) LEFT JOIN ${domains.table} AS ${as_domain} USING (${domains.domainid})";
}

static public class BASERELATIONS_SENSES_WORDS_X_BY_SYNSET {
	static public final String TABLE = "( ${MAKEQUERY} ) AS ${as_relation} INNER JOIN ${relations.table} USING (${relations.relationid}) INNER JOIN ${synsets.table} AS ${as_dest} ON ${as_relation}.${baserelations.synset2id} = ${as_dest}.${synsets.synsetid} LEFT JOIN ${senses.table} ON ${as_dest}.${synsets.synsetid} = ${senses.table}.${senses.synsetid} LEFT JOIN ${words.table} AS ${as_word} USING (${words.wordid}) LEFT JOIN ${words.table} AS ${as_word2} ON ${as_relation}.${baserelations.word2id} = ${as_word2}.${words.wordid}";
	static public final String GROUPBY = "${d_synsetid},${as_type},${relations.relation},${relations.relationid},${d_wordid},${d_word}";
}

static public class SEMRELATIONS_SYNSETS {
	static public final String TABLE = "${semrelations.table} AS ${as_relation} INNER JOIN ${synsets.table} AS ${as_dest} ON ${as_relation}.${semrelations.synset2id} = ${as_dest}.${synsets.synsetid}";
}

static public class SEMRELATIONS_SYNSETS_X {
	static public final String TABLE = "${semrelations.table} AS ${as_relation} INNER JOIN ${relations.table} USING (${relations.relationid}) INNER JOIN ${synsets.table} AS ${as_dest} ON ${as_relation}.${semrelations.synset2id} = ${as_dest}.${synsets.synsetid} ";
}

static public class SEMRELATIONS_SYNSETS_WORDS_X_BY_SYNSET {
	static public final String TABLE = "${semrelations.table} AS ${as_relation} INNER JOIN ${relations.table} USING (${relations.relationid}) INNER JOIN ${synsets.table} AS ${as_dest} ON ${as_relation}.${semrelations.synset2id} = ${as_dest}.${synsets.synsetid} LEFT JOIN ${senses.table} ON ${as_dest}.${synsets.synsetid} = ${senses.table}.${senses.synsetid} LEFT JOIN ${words.table} USING (${words.wordid})";
	static public final String[] PROJECTION = {"*","GROUP_CONCAT(${words.table}.${words.word}, ', ' ) AS ${semrelations_synsets_words_x.members2}"};
	static public final String GROUPBY = "${as_dest}.${synsets.synsetid}";
}

static public class LEXRELATIONS_SENSES {
	static public final String TABLE = "${lexrelations.table} AS ${as_relation} INNER JOIN ${synsets.table} AS ${as_dest} ON ${as_relation}.${lexrelations.synset2id} = ${as_dest}.${synsets.synsetid} INNER JOIN ${words.table} AS ${as_word} ON ${as_relation}.${lexrelations.word2id} = ${as_word}.${words.wordid}";
}

static public class LEXRELATIONS_SENSES_X {
	static public final String TABLE = "${lexrelations.table} AS ${as_relation} INNER JOIN ${relations.table} USING (${relations.relationid}) INNER JOIN ${synsets.table} AS ${as_dest} ON ${as_relation}.${lexrelations.synset2id} = ${as_dest}.${synsets.synsetid} INNER JOIN ${words.table} AS ${as_word} ON ${as_relation}.${lexrelations.word2id} = ${as_word}.${words.wordid} ";
}

static public class LEXRELATIONS_SENSES_WORDS_X_BY_SYNSET {
	static public final String TABLE = "${lexrelations.table} AS ${as_relation} INNER JOIN ${relations.table} USING (${relations.relationid}) INNER JOIN ${synsets.table} AS ${as_dest} ON ${as_relation}.${lexrelations.synset2id} = ${as_dest}.${synsets.synsetid} INNER JOIN ${words.table} AS ${as_word} ON ${as_relation}.${lexrelations.word2id} = ${as_word}.${words.wordid} LEFT JOIN ${senses.table} AS ${as_sense} ON ${as_dest}.${senses.synsetid} = ${as_sense}.${senses.synsetid} LEFT JOIN ${words.table} AS ${as_word2} USING (${words.wordid})";
	static public final String[] PROJECTION = {"*","GROUP_CONCAT(DISTINCT ${as_word2}.${words.word}) AS ${lexrelations_senses_words_x.members2}"};
	static public final String GROUPBY = "${as_dest}.${synsets.synsetid}";
}

static public class SENSES_VFRAMES {
	static public final String TABLE = "${senses_vframes.table} LEFT JOIN ${vframes.table} USING (${vframes.frameid})";
}

static public class SENSES_VTEMPLATES {
	static public final String TABLE = "${senses_vtemplates.table} LEFT JOIN ${vtemplates.table} USING (${vtemplates.templateid})";
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
