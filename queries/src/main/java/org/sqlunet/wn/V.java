package org.sqlunet.wn;
public class V {
static public class Suggest_FTS_Samples {
static public final String SEARCH_SAMPLE_PATH = "${suggest_fts_samples.search_sample_path}";
static public final String TABLE = "${suggest_fts_samples.table}";
}
static public class Suggest_FTS_Definitions {
static public final String SEARCH_DEFINITION_PATH = "${suggest_fts_definitions.search_definition_path}";
static public final String TABLE = "${suggest_fts_definitions.table}";
}
static public class Suggest_FTS_Words {
static public final String SEARCH_WORD_PATH = "${suggest_fts_words.search_word_path}";
static public final String TABLE = "${suggest_fts_words.table}";
}
static public class Suggest_Words {
static public final String SEARCH_WORD_PATH = "${suggest_words.search_word_path}";
static public final String TABLE = "${suggest_words.table}";
}
static public class Lookup_Samples {
static public final String TABLE = "${lookup_samples.table}";
static public final String SYNSETID = "${lookup_samples.synsetid}";
static public final String SAMPLE = "${lookup_samples.sample}";
}
static public class Lookup_Definitions {
static public final String TABLE = "${lookup_definitions.table}";
static public final String SYNSETID = "${lookup_definitions.synsetid}";
static public final String DEFINITION = "${lookup_definitions.definition}";
}
static public class Lookup_Words {
static public final String TABLE = "${lookup_words.table}";
static public final String WORDID = "${lookup_words.wordid}";
static public final String WORD = "${lookup_words.word}";
}
static public class Dict {
static public final String TABLE = "${dict.table}";
}
static public class Words_Lexes_Morphs {
static public final String TABLE = "${words_lexes_morphs.table}";
static public final String TABLE_BY_WORD = "${words_lexes_morphs.table_by_word}";
static public final String CONTENT_URI_TABLE_BY_WORD = "${words_lexes_morphs.content_uri_table_by_word}";
static public final String WORD = "${words_lexes_morphs.word}";
static public final String WORDID = "${words_lexes_morphs.wordid}";
static public final String MORPH = "${words_lexes_morphs.morph}";
static public final String POSID = "${words_lexes_morphs.posid}";
}
static public class Lexes_Morphs {
static public final String TABLE = "${lexes_morphs.table}";
static public final String WORDID = "${lexes_morphs.wordid}";
static public final String MORPH = "${lexes_morphs.morph}";
static public final String POSID = "${lexes_morphs.posid}";
}
static public class Senses_AdjPositions {
static public final String TABLE = "${senses_adjpositions.table}";
static public final String SYNSETID = "${senses_adjpositions.synsetid}";
static public final String WORDID = "${senses_adjpositions.wordid}";
static public final String POSITIONID = "${senses_adjpositions.positionid}";
static public final String POSITION = "${senses_adjpositions.position}";
}
static public class Senses_VTemplates {
static public final String TABLE = "${senses_vtemplates.table}";
static public final String SYNSETID = "${senses_vtemplates.synsetid}";
static public final String WORDID = "${senses_vtemplates.wordid}";
static public final String TEMPLATE = "${senses_vtemplates.template}";
}
static public class Senses_VFrames {
static public final String TABLE = "${senses_vframes.table}";
static public final String SYNSETID = "${senses_vframes.synsetid}";
static public final String WORDID = "${senses_vframes.wordid}";
static public final String FRAME = "${senses_vframes.frame}";
}
static public class LexRelations_Senses_Words_X {
static public final String TABLE_BY_SYNSET = "${lexrelations_senses_words_x.table_by_synset}";
static public final String SYNSET1ID = "${lexrelations_senses_words_x.synset1id}";
static public final String SYNSET2ID = "${lexrelations_senses_words_x.synset2id}";
static public final String MEMBERS2 = "${members2}";
}
static public class LexRelations_Senses_X {
static public final String TABLE = "${lexrelations_senses_x.table}";
}
static public class LexRelations_Senses {
static public final String TABLE = "${lexrelations_senses.table}";
}
static public class SemRelations_Synsets_Words_X {
static public final String TABLE_BY_SYNSET = "${semrelations_synsets_words_x.table_by_synset}";
static public final String SYNSET1ID = "${semrelations_synsets_words_x.synset1id}";
static public final String SYNSET2ID = "${semrelations_synsets_words_x.synset2id}";
static public final String MEMBERS2 = "${members2}";
static public final String RECURSES = "${semrelations_synsets_words_x.recurses}";
}
static public class SemRelations_Synsets_X {
static public final String TABLE = "${semrelations_synsets_x.table}";
}
static public class SemRelations_Synsets {
static public final String TABLE = "${semrelations_synsets.table}";
}
static public class BaseRelations_Senses_Words_X {
static public final String TABLE_BY_SYNSET = "${baserelations_senses_words_x.table_by_synset}";
static public final String SYNSET1ID = "${baserelations_senses_words_x.synset1id}";
static public final String SYNSET2ID = "${baserelations_senses_words_x.synset2id}";
static public final String MEMBERS2 = "${members2}";
static public final String RECURSES = "${baserelations_senses_words_x.recurses}";
}
static public class Synsets_Poses_Domains {
static public final String TABLE = "${synsets_poses_domains.table}";
static public final String SYNSETID = "${synsets_poses_domains.synsetid}";
}
static public class Senses_Synsets_Poses_Domains {
static public final String TABLE = "${senses_synsets_poses_domains.table}";
}
static public class Senses_Words {
static public final String TABLE = "${senses_words.table}";
static public final String TABLE_BY_SYNSET = "${senses_words.table_by_synset}";
static public final String CONTENT_URI_TABLE_BY_SYNSET = "${senses_words.content_uri_table_by_synset}";
static public final String SYNSETID = "${senses_words.synsetid}";
static public final String WORDID = "${senses_words.wordid}";
static public final String MEMBER = "${member}";
static public final String MEMBERS = "${members}";
}
static public class Words_Senses_CasedWords_Synsets_Poses_Domains {
static public final String TABLE = "${words_senses_casedwords_synsets_poses_domains.table}";
static public final String WORD = "${words_senses_casedwords_synsets_poses_domains.word}";
static public final String CASED = "${words_senses_casedwords_synsets_poses_domains.cased}";
static public final String WORDID = "${words_senses_casedwords_synsets_poses_domains.wordid}";
static public final String SYNSETID = "${words_senses_casedwords_synsets_poses_domains.synsetid}";
static public final String SENSEID = "${words_senses_casedwords_synsets_poses_domains.senseid}";
static public final String SENSENUM = "${words_senses_casedwords_synsets_poses_domains.sensenum}";
static public final String SENSEKEY = "${words_senses_casedwords_synsets_poses_domains.sensekey}";
static public final String DEFINITION = "${words_senses_casedwords_synsets_poses_domains.definition}";
static public final String LEXID = "${words_senses_casedwords_synsets_poses_domains.lexid}";
static public final String DOMAIN = "${words_senses_casedwords_synsets_poses_domains.domain}";
static public final String POSID = "${words_senses_casedwords_synsets_poses_domains.posid}";
static public final String POS = "${words_senses_casedwords_synsets_poses_domains.pos}";
static public final String TAGCOUNT = "${words_senses_casedwords_synsets_poses_domains.tagcount}";
}
static public class Words_Senses_CasedWords_Synsets {
static public final String TABLE = "${words_senses_casedwords_synsets.table}";
}
static public class Words_Senses_Synsets {
static public final String TABLE = "${words_senses_synsets.table}";
}
static public class Morphs {
static public final String TABLE = "${morphs.table}";
static public final String MORPHID = "${morphs.morphid}";
static public final String MORPH = "${morphs.morph}";
}
static public class VTemplates {
static public final String TABLE = "${vtemplates.table}";
static public final String TEMPLATEID = "${vtemplates.templateid}";
static public final String TEMPLATE = "${vtemplates.template}";
}
static public class VFrames {
static public final String TABLE = "${vframes.table}";
static public final String FRAMEID = "${vframes.frameid}";
static public final String FRAME = "${vframes.frame}";
}
static public class Lexes {
static public final String TABLE = "${lexes.table}";
static public final String LEXID = "${lexes.lexid}";
static public final String WORDID = "${lexes.wordid}";
}
static public class Samples {
static public final String TABLE = "${samples.table}";
static public final String SYNSETID = "${samples.synsetid}";
static public final String SAMPLEID = "${samples.sampleid}";
static public final String SAMPLE = "${samples.sample}";
}
static public class Domains {
static public final String TABLE = "${domains.table}";
static public final String DOMAINID = "${domains.domainid}";
static public final String DOMAIN = "${domains.domain}";
static public final String DOMAINNAME = "${domains.domainname}";
static public final String POSID = "${domains.posid}";
}
static public class AdjPositions {
static public final String TABLE = "${adjpositions.table}";
static public final String POSITIONID = "${adjpositions.positionid}";
static public final String POSITION = "${adjpositions.position}";
}
static public class Poses {
static public final String TABLE = "${poses.table}";
static public final String POSID = "${poses.posid}";
static public final String POS = "${poses.pos}";
}
static public class Relations {
static public final String TABLE = "${relations.table}";
static public final String RELATIONID = "${relations.relationid}";
static public final String RELATION = "${relations.relation}";
static public final String RECURSES = "${relations.recurses}";
static public final String RECURSESSTR = "${relations.recursesstr}";
static public final String RECURSESSELECT = "${relations.recursesselect}";
}
static public class LexRelations {
static public final String TABLE = "${senses_senses.table}";
static public final String WORD1ID = "${senses_senses.word1id}";
static public final String SYNSET1ID = "${senses_senses.synset1id}";
static public final String WORD2ID = "${senses_senses.word2id}";
static public final String SYNSET2ID = "${senses_senses.synset2id}";
static public final String RELATIONID = "${senses_senses.relationid}";
}
static public class SemRelations {
static public final String TABLE = "${synsets_synsets.table}";
static public final String SYNSET1ID = "${synsets_synsets.synset1id}";
static public final String SYNSET2ID = "${synsets_synsets.synset2id}";
static public final String RELATIONID = "${synsets_synsets.relationid}";
}
static public class BaseRelations {
static public final String TABLE = "${baserelations.table}";
static public final String WORD1ID = "${baserelations.word1id}";
static public final String SYNSET1ID = "${baserelations.synset1id}";
static public final String WORD2ID = "${baserelations.word2id}";
static public final String SYNSET2ID = "${baserelations.synset2id}";
static public final String RELATIONID = "${baserelations.relationid}";
}
static public class Synsets {
static public final String TABLE = "${synsets.table}";
static public final String SYNSETID = "${synsets.synsetid}";
static public final String POSID = "${synsets.posid}";
static public final String SENSEID = "${synsets.senseid}";
static public final String DOMAINID = "${synsets.domainid}";
static public final String DEFINITION = "${synsets.definition}";
}
static public class Senses {
static public final String TABLE = "${senses.table}";
static public final String WORDID = "${senses.wordid}";
static public final String SYNSETID = "${senses.synsetid}";
static public final String CASEDWORDID = "${senses.casedwordid}";
static public final String SENSEID = "${senses.senseid}";
static public final String SENSENUM = "${senses.sensenum}";
static public final String SENSEKEY = "${senses.sensekey}";
static public final String LEXID = "${senses.lexid}";
static public final String TAGCOUNT = "${senses.tagcount}";
}
static public class CasedWords {
static public final String TABLE = "${casedwords.table}";
static public final String CASEDWORDID = "${casedwords.casedwordid}";
static public final String WORDID = "${casedwords.wordid}";
static public final String CASED = "${casedwords.cased}";
}
static public class Words {
static public final String TABLE = "${words.table}";
static public final String WORDID = "${words.wordid}";
static public final String WORD = "${words.word}";
}
}
