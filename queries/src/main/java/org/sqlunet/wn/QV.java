/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package org.sqlunet.wn;

import org.sqlbuilder.common.Lib;
import org.sqlbuilder.common.Q;

import java.util.Arrays;
import java.util.stream.Collectors;

public class QV implements Q
{
	//# instantiated at runtime
	static public final String URI_LAST = "#{uri_last}";
	static public final String SUBQUERY = "#{query}";
	static public final String SUBQUERY_TARGET_SYNSETID = "#{query_target_synsetid}";
	static public final String SUBQUERY_TARGET_WORDID = "#{query_target_wordid}";
	static public final String SUBQUERY_TARGET_WORD = "#{query_target_word}";

	//# column aliases at runtime
	static public final String MEMBERS = "#{members}";
	static public final String MEMBERS2 = "#{members2}";

	@Override
	public String[] query(String keyname)
	{
		final String last = URI_LAST;

		String table;
		String[] projection = null;
		String selection = null;
		String[] selectionArgs = null;
		String groupBy = null;

		Key key = Key.valueOf(keyname);
		switch (key)
		{
			// T A B L E
			// table uri : last element is table

			case WORDS:
				table = "${words.table}";
				break;
			case SENSES:
				table = "${senses.table}";
				break;
			case SYNSETS:
				table = "${synsets.table}";
				break;
			case SEMRELATIONS:
				table = "${synsets_synsets.table}";
				break;
			case LEXRELATIONS:
				table = "${senses_senses.table}";
				break;
			case RELATIONS:
				table = "${relations.table}";
				break;
			case POSES:
				table = "${poses.table}";
				break;
			case DOMAINS:
				table = "${domains.table}";
				break;
			case ADJPOSITIONS:
				table = "${adjpositions.table}";
				break;
			case SAMPLES:
				table = "${samples.table}";
				break;

			// I T E M
			// the incoming URI was for a single item because this URI was for a single row, the _ID value part is present.
			// get the last path segment from the URI: this is the _ID value. then, append the value to the WHERE clause for the query

			case WORD:
				table = "${words.table}";
				break;

			case SENSE:
				table = "${senses.table}";
				break;

			case SYNSET:
				table = "${synsets.table}";
				break;

			// V I E W S

			case DICT:
				table = "${dict.table}";
				break;

			// J O I N S

			case WORDS_SENSES_SYNSETS:
				table = String.format("%s AS %s " + //
								"LEFT JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s USING (%s)", //
						"${words.table}", "${as_words}", //
						"${senses.table}", "${as_senses}", "${senses.wordid}", //
						"${synsets.table}", "${as_synsets}", "${synsets.synsetid}");
				break;

			case WORDS_SENSES_CASEDWORDS_SYNSETS:
				table = String.format("%s AS %s " + //
								"LEFT JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s USING (%s,%s) " + //
								"LEFT JOIN %s AS %s USING (%s)", //
						"${words.table}", "${as_words}", //
						"${senses.table}", "${as_senses}", "${senses.wordid}", //
						"${casedwords.table}", "${as_caseds}", "${casedwords.wordid}", "${casedwords.casedwordid}", //
						"${synsets.table}", "${as_synsets}", "${synsets.synsetid}");
				break;

			case WORDS_SENSES_CASEDWORDS_SYNSETS_POSTYPES_LEXDOMAINS:
				table = String.format("%s AS %s " + //
								"LEFT JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s USING (%s,%s) " + //
								"LEFT JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s USING (%s)", //
						"${words.table}", "${as_words}", //
						"${senses.table}", "${as_senses}", "${senses.wordid}", //
						"${casedwords.table}", "${as_caseds}", "${casedwords.wordid}", "${casedwords.casedwordid}", //
						"${synsets.table}", "${as_synsets}", "${synsets.synsetid}", //
						"${poses.table}", "${as_poses}", "${poses.posid}", //
						"${domains.table}", "${as_domains}", "${domains.domainid}");
				break;

			case SENSES_WORDS:
				table = String.format("%s AS %s " + //
								"LEFT JOIN %s AS %s USING (%s)", //
						"${senses.table}", "${as_senses}", //
						"${words.table}", "${as_words}", "${senses.wordid}");
				break;

			case SENSES_WORDS_BY_SYNSET:
				table = String.format("%s AS %s " + //
								"LEFT JOIN %s AS %s USING (%s)", //
						"${senses.table}", "${as_senses}", //
						"${words.table}", "${as_words}", "${words.wordid}");
				projection = new String[]{String.format("GROUP_CONCAT(%s.%s, ', ' ) AS %s", "${words.table}", "${words.word}", MEMBERS)};
				groupBy = "${synsets.synsetid}";
				break;

			case SENSES_SYNSETS_POSES_DOMAINS:
				table = String.format("%s AS %s " + //
								"INNER JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s USING (%s)", //
						"${senses.table}", "${as_senses}", //
						"${synsets.table}", "${as_synsets}", "${synsets.synsetid}", //
						"${poses.table}", "${as_poses}", "${poses.posid}", //
						"${domains.table}", "${as_domains}", "${domains.domainid}");
				break;

			case SYNSETS_POSES_DOMAINS:
				table = String.format("%s AS %s " + //
								"LEFT JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s USING (%s)", //
						"${synsets.table}", "${as_synsets}", //
						"${poses.table}", "${as_poses}", "${poses.posid}",//
						"${domains.table}", "${as_domains}", "${domains.domainid}");
				break;

			case BASERELATIONS_SENSES_WORDS_X_BY_SYNSET:
			{
				table = String.format("( %s ) AS %s " + // 1
								"INNER JOIN %s USING (%s) " + // 2
								"INNER JOIN %s AS %s ON %s.%s = %s.%s " + // 3
								"LEFT JOIN %s ON %s.%s = %s.%s " + // 4
								"LEFT JOIN %s AS %s USING (%s) " + // 5
								"LEFT JOIN %s AS %s ON %s.%s = %s.%s", // 6
						SUBQUERY, "${as_relations}", // 1
						"${relations.table}", "${relations.relationid}", // 2
						"${synsets.table}", "${as_synsets2}", "${as_relations}", "${baserelations.synset2id}", "${as_synsets2}", "${synsets.synsetid}", // 3
						"${senses.table}", "${as_synsets2}", "${synsets.synsetid}", "${senses.table}", "${senses.synsetid}", // 4
						"${words.table}", "${as_words}", "${words.wordid}", //
						"${words.table}", "${as_words2}", "${as_relations}", "${baserelations.word2id}", "${as_words2}", "${words.wordid}");
				selection = null;
				groupBy = String.format("%s,%s,%s,%s,%s,%s", SUBQUERY_TARGET_SYNSETID, "${as_types}", "${relations.relation}", "${relations.relationid}", SUBQUERY_TARGET_WORDID, SUBQUERY_TARGET_WORD);
			}
			break;

			case SEMRELATIONS_SYNSETS:
				table = String.format("%s AS %s " + //
								"INNER JOIN %s AS %s ON %s.%s = %s.%s", //
						"${synsets_synsets.table}", "${as_relations}", //
						"${synsets.table}", "${as_synsets2}", "${as_relations}", "${synsets_synsets.synset2id}", "${as_synsets2}", "${synsets.synsetid}");
				break;

			case SEMRELATIONS_SYNSETS_X:
				table = String.format("%s AS %s " + //
								"INNER JOIN %s USING (%s) " + //
								"INNER JOIN %s AS %s ON %s.%s = %s.%s ", //
						"${synsets_synsets.table}", "${as_relations}", //
						"${relations.table}", "${relations.relationid}", //
						"${synsets.table}", "${as_synsets2}", "${as_relations}", "${synsets_synsets.synset2id}", "${as_synsets2}", "${synsets.synsetid}");
				break;

			case SEMRELATIONS_SYNSETS_WORDS_X_BY_SYNSET:
				table = String.format("%s AS %s " + // 1
								"INNER JOIN %s USING (%s) " + // 2
								"INNER JOIN %s AS %s ON %s.%s = %s.%s " + // 3
								"LEFT JOIN %s ON %s.%s = %s.%s " + // 4
								"LEFT JOIN %s USING (%s)", // 5
						"${synsets_synsets.table}", "${as_relations}", // 1
						"${relations.table}", "${relations.relationid}", // 2
						"${synsets.table}", "${as_synsets2}", "${as_relations}", "${synsets_synsets.synset2id}", "${as_synsets2}", "${synsets.synsetid}", // 3
						"${senses.table}", "${as_synsets2}", "${synsets.synsetid}", "${senses.table}", "${senses.synsetid}", // 4
						"${words.table}", "${words.wordid}"); //5
				projection =  new String[]{ String.format("GROUP_CONCAT(%s.%s, ', ' ) AS %s", "${words.table}", "${words.word}", MEMBERS2)};
				groupBy = String.format("%s.%s", "${as_synsets2}", "${synsets.synsetid}");
				break;

			case LEXRELATIONS_SENSES:
				table = String.format("%s AS %s " + //
								"INNER JOIN %s AS %s ON %s.%s = %s.%s " + //
								"INNER JOIN %s AS %s ON %s.%s = %s.%s", //
						"${senses_senses.table}", "${as_relations}", //
						"${synsets.table}", "${as_synsets2}", "${as_relations}", "${senses_senses.synset2id}", "${as_synsets2}", "${synsets.synsetid}", //
						"${words.table}", "${as_words}", "${as_relations}", "${senses_senses.word2id}", "${as_words}", "${words.wordid}");
				break;

			case LEXRELATIONS_SENSES_X:
				table = String.format("%s AS %s " + //
								"INNER JOIN %s USING (%s) " + //
								"INNER JOIN %s AS %s ON %s.%s = %s.%s " + //
								"INNER JOIN %s AS %s ON %s.%s = %s.%s ", //
						"${senses_senses.table}", "${as_relations}", //
						"${relations.table}", "${relations.relationid}", //
						"${synsets.table}", "${as_synsets2}", "${as_relations}", "${senses_senses.synset2id}", "${as_synsets2}", "${synsets.synsetid}", //
						"${words.table}", "${as_words}", "${as_relations}", "${senses_senses.word2id}", "${as_words}", "${words.wordid}");
				break;

			case LEXRELATIONS_SENSES_WORDS_X_BY_SYNSET:
				table = String.format("%s AS %s " + // 1
								"INNER JOIN %s USING (%s) " + // 2
								"INNER JOIN %s AS %s ON %s.%s = %s.%s " + // 3
								"INNER JOIN %s AS %s ON %s.%s = %s.%s " + // 4
								"LEFT JOIN %s AS %s ON %s.%s = %s.%s " + // 5
								"LEFT JOIN %s AS %s USING (%s)", // 6
						"${senses_senses.table}", "${as_relations}", // 1
						"${relations.table}", "${relations.relationid}", // 2
						"${synsets.table}", "${as_synsets2}", "${as_relations}", "${senses_senses.synset2id}", "${as_synsets2}", "${synsets.synsetid}", // 3
						"${words.table}", "${as_words}", "${as_relations}", "${senses_senses.word2id}", "${as_words}", "${words.wordid}", // 4
						"${senses.table}", "${as_senses}", "${as_synsets2}", "${senses.synsetid}", "${as_senses}", "${senses.synsetid}", // 5
						"${words.table}", "${as_words2}", "${words.wordid}"); //6
				projection =  new String[]{ String.format("GROUP_CONCAT(DISTINCT %s.%s) AS %s", "${as_words2}", "${words.word}", MEMBERS2)};
				groupBy = String.format("%s.%s", "${as_synsets2}", "${synsets.synsetid}");
				break;

			case SENSES_VFRAMES:
				table = String.format("%s " + //
								"LEFT JOIN %s USING (%s)", //
						"${senses_vframes.table}", //
						"${vframes.table}", "${vframes.frameid}");
				break;

			case SENSES_VTEMPLATES:
				table = String.format("%s " + //
								"LEFT JOIN %s USING (%s)", //
						"${senses_vtemplates.table}", //
						"${vtemplates.table}", "${vtemplates.templateid}");
				break;

			case SENSES_ADJPOSITIONS:
				table = String.format("%s " + //
								"LEFT JOIN %s USING (%s)", //
						"${senses_adjpositions.table}", //
						"${adjpositions.table}", "${adjpositions.positionid}");
				break;

			case LEXES_MORPHS:
				table = String.format("%s " + //
								"LEFT JOIN %s USING (%s)", //
						"${lexes_morphs.table}", //
						"${morphs.table}", "${morphs.morphid}");
				break;

			case WORDS_LEXES_MORPHS_BY_WORD:
				groupBy = "${words.wordid}";
				//$FALL-THROUGH$
				//noinspection fallthrough
			case WORDS_LEXES_MORPHS:
				table = String.format("%s " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s)", //
						"${words.table}", //
						"${lexes_morphs.table}", "${words.wordid}", //
						"${morphs.table}", "${morphs.morphid}");
				break;

			// T E X T S E A R C H

			case LOOKUP_FTS_WORDS:
				table = String.format("%s_%s_fts4", "${words.table}", "${words.word}");
				break;

			case LOOKUP_FTS_DEFINITIONS:
				table = String.format("%s_%s_fts4", "${synsets.table}", "${synsets.definition}");
				break;

			case LOOKUP_FTS_SAMPLES:
				table = String.format("%s_%s_fts4", "${samples.table}", "${samples.sample}");
				break;

			// S U G G E S T

			case SUGGEST_WORDS:
			{
				table = "${words.table}";
				projection = new String[]{ //
						String.format("%s AS _id", "${words.wordid}"), //
						String.format("%s AS %s", "${words.word}", "SearchManager.SUGGEST_COLUMN_TEXT_1"), //
						String.format("%s AS %s", "${words.word}", "SearchManager.SUGGEST_COLUMN_QUERY")};
				selection = String.format("%s LIKE ? || '%%'", "${words.word}");
				selectionArgs = new String[]{last};
				groupBy = null;
				break;
			}

			case SUGGEST_FTS_WORDS:
			{
				table = String.format("%s_%s_fts4", "@{words.table}", "@{words.word}");
				projection = new String[]{ //
						String.format("%s AS _id", "${words.wordid}"), //
						String.format("%s AS %s", "${words.word}", "SearchManager.SUGGEST_COLUMN_TEXT_1"), //
						String.format("%s AS %s", "${words.word}", "SearchManager.SUGGEST_COLUMN_QUERY")}; //
				selection = String.format("%s MATCH ?", "${words.word}");
				selectionArgs = new String[]{last + '*'};
				groupBy = null;
				break;
			}

			case SUGGEST_FTS_DEFINITIONS:
			{
				table = String.format("%s_%s_fts4", "@{synsets.table}", "@{synsets.definition}");
				projection = new String[]{ //
						String.format("%s AS _id", "${synsets.synsetid}"), //
						String.format("%s AS %s", "${synsets.definition}", "SearchManager.SUGGEST_COLUMN_TEXT_1"), //
						String.format("%s AS %s", "${synsets.definition}", "SearchManager.SUGGEST_COLUMN_QUERY")};
				selection = String.format("%s MATCH ?", "${synsets.definition}");
				selectionArgs = new String[]{last + '*'};
				groupBy = null;
				break;
			}

			case SUGGEST_FTS_SAMPLES:
			{
				table = String.format("%s_%s_fts4", "@{samples.table}", "@{samples.sample}");
				projection = new String[]{ //
						String.format("%s AS _id", "${samples.sampleid}"), //
						String.format("%s AS %s", "${samples.sample}", "SearchManager.SUGGEST_COLUMN_TEXT_1"), //
						String.format("%s AS %s", "${samples.sample}", "SearchManager.SUGGEST_COLUMN_QUERY")};
				selection = String.format("%s MATCH ?", "${samples.sample}");
				selectionArgs = new String[]{last + '*'};
				groupBy = null;
				break;
			}

			default:
				return null;
		}
		return new String[]{ //
				Lib.quote(table), //
				projection == null ? null : "{" + Arrays.stream(projection).map(Lib::quote).collect(Collectors.joining(",")) + "}", //
				Lib.quote(selection), //
				selectionArgs == null ? null : "{" + Arrays.stream(selectionArgs).map(Lib::quote).collect(Collectors.joining(",")) + "}", //
				Lib.quote(groupBy)};
	}

	public enum Key
	{
		WORDS, SENSES, SYNSETS, SEMRELATIONS, LEXRELATIONS, RELATIONS, POSES, DOMAINS, ADJPOSITIONS, SAMPLES, WORD, SENSE, SYNSET, DICT, WORDS_SENSES_SYNSETS, WORDS_SENSES_CASEDWORDS_SYNSETS, WORDS_SENSES_CASEDWORDS_SYNSETS_POSTYPES_LEXDOMAINS, SENSES_WORDS, SENSES_WORDS_BY_SYNSET, SENSES_SYNSETS_POSES_DOMAINS, SYNSETS_POSES_DOMAINS, BASERELATIONS_SENSES_WORDS_X_BY_SYNSET, SEMRELATIONS_SYNSETS, SEMRELATIONS_SYNSETS_X, SEMRELATIONS_SYNSETS_WORDS_X_BY_SYNSET, LEXRELATIONS_SENSES, LEXRELATIONS_SENSES_X, LEXRELATIONS_SENSES_WORDS_X_BY_SYNSET, SENSES_VFRAMES, SENSES_VTEMPLATES, SENSES_ADJPOSITIONS, LEXES_MORPHS, WORDS_LEXES_MORPHS_BY_WORD, WORDS_LEXES_MORPHS, LOOKUP_FTS_WORDS, LOOKUP_FTS_DEFINITIONS, LOOKUP_FTS_SAMPLES, SUGGEST_WORDS, SUGGEST_FTS_WORDS, SUGGEST_FTS_DEFINITIONS, SUGGEST_FTS_SAMPLES,
	}
}
