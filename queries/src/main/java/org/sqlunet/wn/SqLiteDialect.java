/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package org.sqlunet.wn;

/**
 * WordNet SQL dialect
 *
 * @author <a href="mailto:1313ou@gmail.com">Bernard Bou</a>
 */
class SqLiteDialect
{
	// query for lexical domains enumeration
	static final String AllLexDomainsQuery = "SELECT ${domains.domainid}, ${domains.domain}, ${domains.posid} " +  //
			"FROM ${domains.table} " +  //
			"ORDER BY ${domains.domainid};";

	// query for links enumeration
	static final String AllLinksQuery = "SELECT ${relations.relationid}, ${relations.relation}, ${relations.recurses} " +  //
			"FROM ${relations.table} " +  //
			"ORDER BY ${relations.relation};";

	// WORD

	// query for word id
	static final String WordQuery = "SELECT ${words.wordid}, ${words.word} " +  //
			"FROM ${words.table} " +  //
			"WHERE ${words.wordid} = ?;";

	// query for word
	static final String WordQueryFromLemma = "SELECT ${words.wordid}, ${words.word} " +  //
			"FROM ${words.table} " +  //
			"WHERE ${words.word} = ?;";

	// SYNSET

	// query for synset from synset id
	static final String SynsetQuery = "SELECT ${synsets.synsetid}, ${synsets.definition}, ${synsets.domainid}, GROUP_CONCAT(${samples.sample}, '|' ) AS ${sampleset} " +  //
			"FROM ${synsets.table} " +  //
			"LEFT JOIN ${samples.table} USING (${synsets.synsetid}) " +  //
			"WHERE ${synsets.synsetid} = ? " +  //
			"GROUP BY ${synsets.synsetid};";

	// query for words in synsets
	static final String SynsetWordsQuery = "SELECT ${words.word}, ${words.wordid} " +  //
			"FROM ${senses.table} " +  //
			"INNER JOIN ${words.table} USING (${words.wordid}) " + //
			"WHERE ${senses.synsetid} = ?;";

	// query for synsets from word id
	static final String SynsetsQueryFromWordId = "SELECT ${synsets.synsetid}, ${synsets.definition}, ${synsets.posid}, ${synsets.domainid}, GROUP_CONCAT(${samples.sample}, '|' ) AS ${sampleset} " +  //
			"FROM ${senses.table} " +  //
			"INNER JOIN ${synsets.table} USING (${synsets.synsetid}) " + //
			"LEFT JOIN ${samples.table} USING (${synsets.synsetid}) " + //
			"WHERE ${senses.wordid} = ? " + //
			"GROUP BY ${synsets.synsetid} " +  //
			"ORDER BY ${synsets.domainid} ASC, ${senses.sensenum} DESC;";

	// query for synsets of given pos id from word id
	static final String SynsetsQueryFromWordIdAndPos = "SELECT ${synsets.synsetid}, ${synsets.definition}, ${synsets.domainid}, GROUP_CONCAT(${samples.sample}, '|' ) AS ${sampleset} " + //
			"FROM ${senses.table} " + //
			"INNER JOIN ${synsets.table} USING (${synsets.synsetid}) " + //
			"LEFT JOIN ${samples.table} USING (synsets.synsetid) " + //
			"WHERE ${senses.wordid} = ? AND ${synsets.posid} = ? " + //
			"GROUP BY ${synsets.synsetid} " + //
			"ORDER BY ${synsets.domainid} ASC, ${senses.sensenum} ASC;";

	// query for synsets of given lexdomain from word
	static final String SynsetsQueryFromWordIdAndLexDomainId = "SELECT ${synsets.synsetid}, ${synsets.definition}, ${synsets.domainid}, GROUP_CONCAT(${samples.sample}, '|' ) AS ${sampleset} " + //
			"FROM ${senses.table} " + //
			"INNER JOIN ${synsets.table} USING (${synsets.synsetid}) " + //
			"LEFT JOIN ${samples.table} USING (${synsets.synsetid}) " + //
			"WHERE ${senses.wordid} = ? AND ${synsets.domainid} = ? " + //
			"GROUP BY ${synsets.synsetid} " + //
			"ORDER BY ${synsets.domainid} ASC, ${senses.sensenum} ASC;";

	// LINKS
	// query for links from synsets
	static final String LinksQueryFromSynsetId = "SELECT ${relations.relationid}, ${synsets.synsetid}, ${synsets.definition}, ${synsets.domainid}, GROUP_CONCAT(${samples.sample}, '|' ) AS ${sampleset}, 0 AS ${senses_senses.word2id}, NULL AS ${word2}, ${synsets_synsets.synset1id}, 0 " +  //
			"FROM ${synsets_synsets.table} " + //
			"INNER JOIN ${synsets.table} ON ${synsets_synsets.synset2id} = ${synsets.synsetid} " +  //
			"LEFT JOIN ${relations.table} USING (${relations.relationid}) " + //
			"LEFT JOIN ${samples.table} USING (${synsets.synsetid}) " + //
			"WHERE ${synsets_synsets.synset1id} = ? " + //
			"GROUP BY ${synsets.synsetid} " + //
			"UNION " + //
			"SELECT ${relations.relationid}, ${synsets.synsetid}, ${synsets.definition}, ${synsets.domainid}, GROUP_CONCAT(${samples.sample}, '|' ) AS ${sampleset}, GROUP_CONCAT(DISTINCT ${senses_senses.word2id}), GROUP_CONCAT(DISTINCT ${words.word}) AS ${word2}, ${senses_senses.synset1id}, ${senses_senses.word1id} " + ///
			"FROM ${senses_senses.table} " + //
			"INNER JOIN ${synsets.table} ON ${senses_senses.synset2id} = ${synsets.synsetid} " + //
			"LEFT JOIN ${words.table} ON ${senses_senses.word2id} = ${words.wordid} " + //
			"LEFT JOIN ${relations.table} USING (${relations.relationid}) " + //
			"LEFT JOIN ${samples.table} USING (${synsets.synsetid}) " + //
			"WHERE ${senses_senses.synset1id} = ? AND CASE ? WHEN 0 THEN 1 ELSE ${senses_senses.word1id} = ? END " + //
			"GROUP BY ${synsets.synsetid} " + //
			"ORDER BY 1, 2;";

	// query for links from synsets and link type
	static final String LinksQueryFromSynsetIdAndLinkId = //
			"SELECT ${relations.relationid}, ${synsets.synsetid}, ${synsets.definition}, ${synsets.domainid}, GROUP_CONCAT(${samples.sample}, '|' ) AS ${sampleset}, 0 AS ${senses_senses.word2id}, NULL AS ${word2}, ${synsets_synsets.synset1id}, 0 " + //
			"FROM ${synsets_synsets.table} " + //
			"INNER JOIN ${synsets.table} ON ${synsets_synsets.synset2id} = ${synsets.synsetid} " + //
			"LEFT JOIN ${relations.table} USING (${relations.relationid}) " + //
			"LEFT JOIN ${samples.table} USING (${synsets.synsetid}) " + //
			"WHERE ${synsets_synsets.synset1id} = ? AND ${relations.relationid} = ? " + //
			"GROUP BY ${synsets.synsetid} " + //
			"UNION " + //
			"SELECT ${relations.relationid}, ${synsets.synsetid}, ${synsets.definition}, ${synsets.domainid}, GROUP_CONCAT(${samples.sample}, '|' ) AS ${sampleset}, GROUP_CONCAT(DISTINCT ${senses_senses.word2id}), GROUP_CONCAT(DISTINCT ${words.word}) AS ${word2}, ${senses_senses.synset1id}, ${senses_senses.word1id} " + //
			"FROM ${senses_senses.table} " + //
			"INNER JOIN ${synsets.table} ON ${senses_senses.synset2id} = ${synsets.synsetid} " + //
			"LEFT JOIN ${words.table} ON ${senses_senses.word2id} = ${words.wordid} " + //
			"LEFT JOIN ${relations.table} USING (${relations.relationid}) " + //
			"LEFT JOIN ${samples.table} USING (${synsets.synsetid}) " + //
			"WHERE ${senses_senses.synset1id} = ? AND ${relations.relationid} = ? AND CASE ? WHEN 0 THEN 1 ELSE ${senses_senses.word1id} = ? END " + //
			"GROUP BY ${synsets.synsetid} " + //
			"ORDER BY 1, 2;";

	// query for link types of a word
	static final String LinkTypesQueryFromWord = //
			"SELECT ${synsets_synsets.relationid}, ${synsets.domainid} " + //
			"FROM ${words.table} " + //
			"LEFT JOIN ${senses.table} USING (${words.wordid}) " + //
			"LEFT JOIN ${synsets.table} USING (${synsets.synsetid}) " + //
			"LEFT JOIN ${synsets_synsets.table} ON ${synsets.synsetid} = ${synsets_synsets.synset1id} " + //
			"WHERE ${words.word} = ? " + //
			"GROUP BY ${synsets.domainid}, ${synsets_synsets.relationid} " + //
			"UNION " + //
			"SELECT ${senses_senses.relationid}, ${synsets.domainid} " + //
			"FROM ${words.table} " + //
			"LEFT JOIN ${senses.table} USING (${words.wordid}) " + //
			"LEFT JOIN ${synsets.table} USING (${synsets.synsetid}) " + //
			"LEFT JOIN ${senses_senses.table} ON ${synsets.synsetid} = ${senses_senses.synset1id} AND ${words.wordid} = ${senses_senses.word1id} " + //
			"WHERE ${words.word} = ? " + //
			"GROUP BY ${synsets.domainid}, ${senses_senses.relationid} " + //
			"ORDER BY 1, 2;";
}
