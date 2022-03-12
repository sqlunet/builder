/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package org.sqlunet.wn;

import org.sqlbuilder.common.Lib;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.sqlbuilder.common.Q;

public class QV implements Q
{
	//static private final String SOURCE_SYNSETID = "${s_synsetid}";
	//static private final String SOURCE_DEFINITION = "${s_definition}";
	//static private final String SOURCE_WORD = "${s_word}";
	//static private final String SOURCE_WORDID = "${s_wordid}";
	static public final String TARGET_SYNSETID = "${d_synsetid}";
	static public final String TARGET_DEFINITION = "${d_definition}";
	static public final String TARGET_WORD = "${d_word}";
	static public final String TARGET_WORDID = "${d_wordid}";
	static public final String MEMBERS = "${members}";
	static public final String MEMBERS2 = "${members2}";

	@Override
	public String[] query(String key)
	{
		final String last = "${uri_last}";
		final String[] projection = null;
		final String selection = null;
		final String[] selectionArgs = null;

		String[] actualProjection = projection;
		String actualSelection = selection;
		String[] actualSelectionArgs = selectionArgs;
		String groupBy = null;
		String table;

		switch (key)
		{
			// T A B L E
			// table uri : last element is table

			case "WORDS":
				table = "${words.table}";
				break;
			case "SENSES":
				table = "${senses.table}";
				break;
			case "SYNSETS":
				table = "${synsets.table}";
				break;
			case "SEMRELATIONS":
				table = "${synsets_synsets.table}";
				break;
			case "LEXRELATIONS":
				table = "${senses_senses.table}";
				break;
			case "RELATIONS":
				table = "${relations.table}";
				break;
			case "POSES":
				table = "${poses.table}";
				break;
			case "DOMAINS":
				table = "${domains.table}";
				break;
			case "ADJPOSITIONS":
				table = "${adjpositions.table}";
				break;
			case "SAMPLES":
				table = "${samples.table}";
				break;

			// I T E M
			// the incoming URI was for a single item because this URI was for a single row, the _ID value part is present.
			// get the last path segment from the URI: this is the _ID value. then, append the value to the WHERE clause for the query

			case "WORD":
				table = "${words.table}";
				break;

			case "SENSE":
				table = "${senses.table}";
				break;

			case "SYNSET":
				table = "${synsets.table}";
				break;

			// V I E W S

			case "DICT":
				table = "URI_PATH_SEGMENT";
				break;

			// J O I N S

			case "WORDS_SENSES_SYNSETS":
				table = String.format("%s AS %s " + //
								"LEFT JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s USING (%s)", //
						"${words.table}", "${as_word}", //
						"${senses.table}", "${as_sense}", "${senses.wordid}", //
						"${synsets.table}", "${as_synset}", "${synsets.synsetid}");
				break;

			case "WORDS_SENSES_CASEDWORDS_SYNSETS":
				table = String.format("%s AS %s " + //
								"LEFT JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s USING (%s,%s) " + //
								"LEFT JOIN %s AS %s USING (%s)", //
						"${words.table}", "${as_word}", //
						"${senses.table}", "${as_sense}", "${senses.wordid}", //
						"${casedwords.table}", "${as_cased}", "${casedwords.wordid}", "${casedwords.casedwordid}", //
						"${synsets.table}", "${as_synset}", "${synsets.synsetid}");
				break;

			case "WORDS_SENSES_CASEDWORDS_SYNSETS_POSTYPES_LEXDOMAINS":
				table = String.format("%s AS %s " + //
								"LEFT JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s USING (%s,%s) " + //
								"LEFT JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s USING (%s)", //
						"${words.table}", "${as_word}", //
						"${senses.table}", "${as_sense}", "${senses.wordid}", //
						"${casedwords.table}", "${as_cased}", "${casedwords.wordid}", "${casedwords.casedwordid}", //
						"${synsets.table}", "${as_synset}", "${synsets.synsetid}", //
						"${poses.table}", "${as_pos}", "${poses.posid}", //
						"${domains.table}", "${as_domain}", "${domains.domainid}");
				break;

			case "SENSES_WORDS":
				table = String.format("%s AS %s " + //
								"LEFT JOIN %s AS %s USING (%s)", //
						"${senses.table}", "${as_sense}", //
						"${words.table}", "${as_word}", "${senses.wordid}");
				break;

			case "SENSES_WORDS_BY_SYNSET":
				table = String.format("%s AS %s " + //
								"LEFT JOIN %s AS %s USING (%s)", //
						"${senses.table}", "${as_sense}", //
						"${words.table}", "${as_word}", "${words.wordid}");
				actualProjection = Lib.appendProjection(actualProjection, String.format("GROUP_CONCAT(%s.%s, ', ' ) AS %s", "${words.table}", "${words.word}", "${senses_words.members}"));
				groupBy = "${synsets.synsetid}";
				break;

			case "SENSES_SYNSETS_POSES_DOMAINS":
				table = String.format("%s AS %s " + //
								"INNER JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s USING (%s)", //
						"${senses.table}", "${as_sense}", //
						"${synsets.table}", "${as_synset}", "${synsets.synsetid}", //
						"${poses.table}", "${as_pos}", "${poses.posid}", //
						"${domains.table}", "${as_domain}", "${domains.domainid}");
				break;

			case "SYNSETS_POSES_DOMAINS":
				table = String.format("%s AS %s " + //
								"LEFT JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s USING (%s)", //
						"${synsets.table}", "${as_synset}", //
						"${poses.table}", "${as_pos}", "${poses.posid}",//
						"${domains.table}", "${as_domain}", "${domains.domainid}");
				break;

			case "BASERELATIONS_SENSES_WORDS_X_BY_SYNSET":
			{
				table = String.format("( %s ) AS %s " + // 1
								"INNER JOIN %s USING (%s) " + // 2
								"INNER JOIN %s AS %s ON %s.%s = %s.%s " + // 3
								"LEFT JOIN %s ON %s.%s = %s.%s " + // 4
								"LEFT JOIN %s AS %s USING (%s) " + // 5
								"LEFT JOIN %s AS %s ON %s.%s = %s.%s", // 6
						"${MAKEQUERY}", "${as_relation}", // 1
						"${relations.table}", "${relations.relationid}", // 2
						"${synsets.table}", "${as_dest}", "${as_relation}", "${baserelations.synset2id}", "${as_dest}", "${synsets.synsetid}", // 3
						"${senses.table}", "${as_dest}", "${synsets.synsetid}", "${senses.table}", "${senses.synsetid}", // 4
						"${words.table}", "${as_word}", "${words.wordid}", //
						"${words.table}", "${as_word2}", "${as_relation}", "${baserelations.word2id}", "${as_word2}", "${words.wordid}");
				actualSelection = null;
				groupBy = String.format("%s,%s,%s,%s,%s,%s", TARGET_SYNSETID, "${as_type}", "${relations.relation}", "${relations.relationid}", TARGET_WORDID, TARGET_WORD);
			}
			break;

			case "SEMRELATIONS_SYNSETS":
				table = String.format("%s AS %s " + //
								"INNER JOIN %s AS %s ON %s.%s = %s.%s", //
						"${semrelations.table}", "${as_relation}", //
						"${synsets.table}", "${as_dest}", "${as_relation}", "${semrelations.synset2id}", "${as_dest}", "${synsets.synsetid}");
				break;

			case "SEMRELATIONS_SYNSETS_X":
				table = String.format("%s AS %s " + //
								"INNER JOIN %s USING (%s) " + //
								"INNER JOIN %s AS %s ON %s.%s = %s.%s ", //
						"${semrelations.table}", "${as_relation}", //
						"${relations.table}", "${relations.relationid}", //
						"${synsets.table}", "${as_dest}", "${as_relation}", "${semrelations.synset2id}", "${as_dest}", "${synsets.synsetid}");
				break;

			case "SEMRELATIONS_SYNSETS_WORDS_X_BY_SYNSET":
				table = String.format("%s AS %s " + // 1
								"INNER JOIN %s USING (%s) " + // 2
								"INNER JOIN %s AS %s ON %s.%s = %s.%s " + // 3
								"LEFT JOIN %s ON %s.%s = %s.%s " + // 4
								"LEFT JOIN %s USING (%s)", // 5
						"${semrelations.table}", "${as_relation}", // 1
						"${relations.table}", "${relations.relationid}", // 2
						"${synsets.table}", "${as_dest}", "${as_relation}", "${semrelations.synset2id}", "${as_dest}", "${synsets.synsetid}", // 3
						"${senses.table}", "${as_dest}", "${synsets.synsetid}", "${senses.table}", "${senses.synsetid}", // 4
						"${words.table}", "${words.wordid}"); //5
				actualProjection = Lib.appendProjection(actualProjection, String.format("GROUP_CONCAT(%s.%s, ', ' ) AS %s", "${words.table}", "${words.word}", "${semrelations_synsets_words_x.members2}"));
				groupBy = String.format("%s.%s", "${as_dest}", "${synsets.synsetid}");
				break;

			case "LEXRELATIONS_SENSES":
				table = String.format("%s AS %s " + //
								"INNER JOIN %s AS %s ON %s.%s = %s.%s " + //
								"INNER JOIN %s AS %s ON %s.%s = %s.%s", //
						"${lexrelations.table}", "${as_relation}", //
						"${synsets.table}", "${as_dest}", "${as_relation}", "${lexrelations.synset2id}", "${as_dest}", "${synsets.synsetid}", //
						"${words.table}", "${as_word}", "${as_relation}", "${lexrelations.word2id}", "${as_word}", "${words.wordid}");
				break;

			case "LEXRELATIONS_SENSES_X":
				table = String.format("%s AS %s " + //
								"INNER JOIN %s USING (%s) " + //
								"INNER JOIN %s AS %s ON %s.%s = %s.%s " + //
								"INNER JOIN %s AS %s ON %s.%s = %s.%s ", //
						"${lexrelations.table}", "${as_relation}", //
						"${relations.table}", "${relations.relationid}", //
						"${synsets.table}", "${as_dest}", "${as_relation}", "${lexrelations.synset2id}", "${as_dest}", "${synsets.synsetid}", //
						"${words.table}", "${as_word}", "${as_relation}", "${lexrelations.word2id}", "${as_word}", "${words.wordid}");
				break;

			case "LEXRELATIONS_SENSES_WORDS_X_BY_SYNSET":
				table = String.format("%s AS %s " + // 1
								"INNER JOIN %s USING (%s) " + // 2
								"INNER JOIN %s AS %s ON %s.%s = %s.%s " + // 3
								"INNER JOIN %s AS %s ON %s.%s = %s.%s " + // 4
								"LEFT JOIN %s AS %s ON %s.%s = %s.%s " + // 5
								"LEFT JOIN %s AS %s USING (%s)", // 6
						"${lexrelations.table}", "${as_relation}", // 1
						"${relations.table}", "${relations.relationid}", // 2
						"${synsets.table}", "${as_dest}", "${as_relation}", "${lexrelations.synset2id}", "${as_dest}", "${synsets.synsetid}", // 3
						"${words.table}", "${as_word}", "${as_relation}", "${lexrelations.word2id}", "${as_word}", "${words.wordid}", // 4
						"${senses.table}", "${as_sense}", "${as_dest}", "${senses.synsetid}", "${as_sense}", "${senses.synsetid}", // 5
						"${words.table}", "${as_word2}", "${words.wordid}"); //6
				actualProjection = Lib.appendProjection(actualProjection, String.format("GROUP_CONCAT(DISTINCT %s.%s) AS %s", "${as_word2}", "${words.word}", "${lexrelations_senses_words_x.members2}"));
				groupBy = String.format("%s.%s", "${as_dest}", "${synsets.synsetid}");
				break;

			case "SENSES_VFRAMES":
				table = String.format("%s " + //
								"LEFT JOIN %s USING (%s)", //
						"${senses_vframes.table}", //
						"${vframes.table}", "${vframes.frameid}");
				break;

			case "SENSES_VTEMPLATES":
				table = String.format("%s " + //
								"LEFT JOIN %s USING (%s)", //
						"${senses_vtemplates.table}", //
						"${vtemplates.table}", "${vtemplates.templateid}");
				break;

			case "SENSES_ADJPOSITIONS":
				table = String.format("%s " + //
								"LEFT JOIN %s USING (%s)", //
						"${senses_adjpositions.table}", //
						"${adjpositions.table}", "${adjpositions.positionid}");
				break;

			case "LEXES_MORPHS":
				table = String.format("%s " + //
								"LEFT JOIN %s USING (%s)", //
						"${lexes_morphs.table}", //
						"${morphs.table}", "${morphs.morphid}");
				break;

			case "WORDS_LEXES_MORPHS_BY_WORD":
				groupBy = "${words.wordid}";
				//$FALL-THROUGH$
				//noinspection fallthrough
			case "WORDS_LEXES_MORPHS":
				table = String.format("%s " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s)", //
						"${words.table}", //
						"${lexes_morphs.table}", "${words.wordid}", //
						"${morphs.table}", "${morphs.morphid}");
				break;

			// T E X T S E A R C H

			case "LOOKUP_FTS_WORDS":
				table = String.format("%s_%s_fts4", "${words.table}", "${words.word}");
				break;

			case "LOOKUP_FTS_DEFINITIONS":
				table = String.format("%s_%s_fts4", "${synsets.table}", "${synsets.definition}");
				break;

			case "LOOKUP_FTS_SAMPLES":
				table = String.format("%s_%s_fts4", "${samples.table}", "${samples.sample}");
				break;

			// S U G G E S T

			case "SUGGEST_WORDS":
			{
				table = "${words.table}";
				actualProjection = new String[]{ //
						String.format("%s AS _id", "${words.wordid}"), //
						String.format("%s AS %s", "${words.word}", "SearchManager.SUGGEST_COLUMN_TEXT_1"), //
						String.format("%s AS %s", "${words.word}", "SearchManager.SUGGEST_COLUMN_QUERY")};
				actualSelection = String.format("%s LIKE ? || '%%'", "${words.word}");
				actualSelectionArgs = new String[]{last};
				groupBy = null;
				break;
			}

			case "SUGGEST_FTS_WORDS":
			{
				table = String.format("%s_%s_fts4", "${words.table}", "${words.word}");
				actualProjection = new String[]{ //
						String.format("%s AS _id", "${words.wordid}"), //
						String.format("%s AS %s", "${words.word}", "SearchManager.SUGGEST_COLUMN_TEXT_1"), //
						String.format("%s AS %s", "${words.word}", "SearchManager.SUGGEST_COLUMN_QUERY")}; //
				actualSelection = String.format("%s MATCH ?", "${words.word}");
				actualSelectionArgs = new String[]{last + '*'};
				groupBy = null;
				break;
			}

			case "SUGGEST_FTS_DEFINITIONS":
			{
				table = String.format("%s_%s_fts4", "${synsets.table}", "${synsets.definition}");
				actualProjection = new String[]{ //
						String.format("%s AS _id", "${synsets.synsetid}"), //
						String.format("%s AS %s", "${synsets.definition}", "SearchManager.SUGGEST_COLUMN_TEXT_1"), //
						String.format("%s AS %s", "${synsets.definition}", "SearchManager.SUGGEST_COLUMN_QUERY")};
				actualSelection = String.format("%s MATCH ?", "${synsets.definition}");
				actualSelectionArgs = new String[]{last + '*'};
				groupBy = null;
				break;
			}

			case "SUGGEST_FTS_SAMPLES":
			{
				table = String.format("%s_%s_fts4", "${samples.table}", "${samples.sample}");
				actualProjection = new String[]{ //
						String.format("%s AS _id", "${samples.sampleid}"), //
						String.format("%s AS %s", "${samples.sample}", "SearchManager.SUGGEST_COLUMN_TEXT_1"), //
						String.format("%s AS %s", "${samples.sample}", "SearchManager.SUGGEST_COLUMN_QUERY")};
				actualSelection = String.format("%s MATCH ?", "${samples.sample}");
				actualSelectionArgs = new String[]{last + '*'};
				groupBy = null;
				break;
			}

			default:
				return null;
		}
		return new String[]{ //
				Lib.quote(table), //
				actualProjection == null ? null : "{" + Arrays.stream(actualProjection).map(Lib::quote).collect(Collectors.joining(",")) + "}", //
				Lib.quote(actualSelection), //
				actualSelectionArgs == null ? null : "{" + Arrays.stream(actualSelectionArgs).map(Lib::quote).collect(Collectors.joining(",")) + "}", //
				Lib.quote(groupBy)};
	}
}
