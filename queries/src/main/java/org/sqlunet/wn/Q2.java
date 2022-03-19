/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package org.sqlunet.wn;

import org.sqlbuilder.common.Lib;

import java.util.Arrays;

import org.sqlunet.wn.C.*;
import org.sqlbuilder.common.Q;

public class Q2 implements Q
{
	//static private final String SOURCE_SYNSETID = "s_synsetid";
	//static private final String SOURCE_DEFINITION = "s_definition";
	//static private final String SOURCE_WORD = "s_word";
	//static private final String SOURCE_WORDID = "s_wordid";
	static public final String TARGET_SYNSETID = "d_synsetid";
	static public final String TARGET_DEFINITION = "d_definition";
	static public final String TARGET_WORD = "d_word";
	static public final String TARGET_WORDID = "d_wordid";

	@Override
	public String[] query(String key)
	{
		final String last = "URILAST";
		final String[] projection = {"PROJECTION"};
		final String selection = "SELECTION";
		final String[] selectionArgs = {"ARGS"};

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
				table = "URI_PATH_SEGMENT";
				break;
			case "SENSES":
				table = "URI_PATH_SEGMENT";
				break;
			case "SYNSETS":
				table = "URI_PATH_SEGMENT";
				break;
			case "SEMRELATIONS":
				table = "URI_PATH_SEGMENT";
				break;
			case "LEXRELATIONS":
				table = "URI_PATH_SEGMENT";
				break;
			case "RELATIONS":
				table = "URI_PATH_SEGMENT";
				break;
			case "POSES":
				table = "URI_PATH_SEGMENT";
				break;
			case "DOMAINS":
				table = "URI_PATH_SEGMENT";
				break;
			case "ADJPOSITIONS":
				table = "URI_PATH_SEGMENT";
				break;
			case "SAMPLES":
				table = "URI_PATH_SEGMENT";
				break;

			// I T E M
			// the incoming URI was for a single item because this URI was for a single row, the _ID value part is present.
			// get the last path segment from the URI: this is the _ID value. then, append the value to the WHERE clause for the query

			case "WORD":
				table = Words.TABLE;
				break;

			case "SENSE":
				table = Senses.TABLE;
				break;

			case "SYNSET":
				table = Synsets.TABLE;
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
						Words.TABLE, C.AS_WORD, //
						Senses.TABLE, C.AS_SENSE, Senses.WORDID, //
						Synsets.TABLE, C.AS_SYNSET, Synsets.SYNSETID);
				break;

			case "WORDS_SENSES_CASEDWORDS_SYNSETS":
				table = String.format("%s AS %s " + //
								"LEFT JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s USING (%s,%s) " + //
								"LEFT JOIN %s AS %s USING (%s)", //
						Words.TABLE, C.AS_WORD, //
						Senses.TABLE, C.AS_SENSE, Senses.WORDID, //
						CasedWords.TABLE, C.AS_CASED, CasedWords.WORDID, CasedWords.CASEDWORDID, //
						Synsets.TABLE, C.AS_SYNSET, Synsets.SYNSETID);
				break;

			case "WORDS_SENSES_CASEDWORDS_SYNSETS_POSTYPES_LEXDOMAINS":
				table = String.format("%s AS %s " + //
								"LEFT JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s USING (%s,%s) " + //
								"LEFT JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s USING (%s)", //
						Words.TABLE, C.AS_WORD, //
						Senses.TABLE, C.AS_SENSE, Senses.WORDID, //
						CasedWords.TABLE, C.AS_CASED, CasedWords.WORDID, CasedWords.CASEDWORDID, //
						Synsets.TABLE, C.AS_SYNSET, Synsets.SYNSETID, //
						Poses.TABLE, C.AS_POS, Poses.POSID, //
						Domains.TABLE, C.AS_DOMAIN, Domains.DOMAINID);
				break;

			case "SENSES_WORDS":
				table = String.format("%s AS %s " + //
								"LEFT JOIN %s AS %s USING (%s)", //
						Senses.TABLE, C.AS_SENSE, //
						Words.TABLE, C.AS_WORD, Senses.WORDID);
				break;

			case "SENSES_WORDS_BY_SYNSET":
				table = String.format("%s AS %s " + //
								"LEFT JOIN %s AS %s USING (%s)", //
						Senses.TABLE, C.AS_SENSE, //
						Words.TABLE, C.AS_WORD, Words.WORDID);
				actualProjection = Lib.appendProjection(actualProjection, String.format("GROUP_CONCAT(%s.%s, ', ' ) AS %s", Words.TABLE, Words.WORD, Senses_Words.MEMBERS));
				groupBy = Synsets.SYNSETID;
				break;

			case "SENSES_SYNSETS_POSES_DOMAINS":
				table = String.format("%s AS %s " + //
								"INNER JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s USING (%s)", //
						Senses.TABLE, C.AS_SENSE, //
						Synsets.TABLE, C.AS_SYNSET, Synsets.SYNSETID, //
						Poses.TABLE, C.AS_POS, Poses.POSID, //
						Domains.TABLE, C.AS_DOMAIN, Domains.DOMAINID);
				break;

			case "SYNSETS_POSES_DOMAINS":
				table = String.format("%s AS %s " + //
								"LEFT JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s USING (%s)", //
						Synsets.TABLE, C.AS_SYNSET, //
						Poses.TABLE, C.AS_POS, Poses.POSID,//
						Domains.TABLE, C.AS_DOMAIN, Domains.DOMAINID);
				break;

			case "BASERELATIONS_SENSES_WORDS_X_BY_SYNSET":
			{
				table = String.format("( %s ) AS %s " + // 1
								"INNER JOIN %s USING (%s) " + // 2
								"INNER JOIN %s AS %s ON %s.%s = %s.%s " + // 3
								"LEFT JOIN %s ON %s.%s = %s.%s " + // 4
								"LEFT JOIN %s AS %s USING (%s) " + // 5
								"LEFT JOIN %s AS %s ON %s.%s = %s.%s", // 6
						"MAKEQUERY", C.AS_RELATION, // 1
						Relations.TABLE, Relations.RELATIONID, // 2
						Synsets.TABLE, C.AS_DEST, C.AS_RELATION, BaseRelations.SYNSET2ID, C.AS_DEST, Synsets.SYNSETID, // 3
						Senses.TABLE, C.AS_DEST, Synsets.SYNSETID, Senses.TABLE, Senses.SYNSETID, // 4
						Words.TABLE, C.AS_WORD, Words.WORDID, //
						Words.TABLE, C.AS_WORD2, C.AS_RELATION, BaseRelations.WORD2ID, C.AS_WORD2, Words.WORDID);
				actualSelection = null;
				groupBy = String.format("%s,%s,%s,%s,%s,%s", TARGET_SYNSETID, C.AS_TYPE, Relations.RELATION, Relations.RELATIONID, TARGET_WORDID, TARGET_WORD);
			}
			break;

			case "SEMRELATIONS_SYNSETS":
				table = String.format("%s AS %s " + //
								"INNER JOIN %s AS %s ON %s.%s = %s.%s", //
						SemRelations.TABLE, C.AS_RELATION, //
						Synsets.TABLE, C.AS_DEST, C.AS_RELATION, SemRelations.SYNSET2ID, C.AS_DEST, Synsets.SYNSETID);
				break;

			case "SEMRELATIONS_SYNSETS_X":
				table = String.format("%s AS %s " + //
								"INNER JOIN %s USING (%s)" + //
								"INNER JOIN %s AS %s ON %s.%s = %s.%s ", //
						SemRelations.TABLE, C.AS_RELATION, //
						Relations.TABLE, Relations.RELATIONID, //
						Synsets.TABLE, C.AS_DEST, C.AS_RELATION, SemRelations.SYNSET2ID, C.AS_DEST, Synsets.SYNSETID);
				break;

			case "SEMRELATIONS_SYNSETS_WORDS_X_BY_SYNSET":
				table = String.format("%s AS %s " + // 1
								"INNER JOIN %s USING (%s) " + // 2
								"INNER JOIN %s AS %s ON %s.%s = %s.%s " + // 3
								"LEFT JOIN %s ON %s.%s = %s.%s " + // 4
								"LEFT JOIN %s USING (%s)", // 5
						SemRelations.TABLE, C.AS_RELATION, // 1
						Relations.TABLE, Relations.RELATIONID, // 2
						Synsets.TABLE, C.AS_DEST, C.AS_RELATION, SemRelations.SYNSET2ID, C.AS_DEST, Synsets.SYNSETID, // 3
						Senses.TABLE, C.AS_DEST, Synsets.SYNSETID, Senses.TABLE, Senses.SYNSETID, // 4
						Words.TABLE, Words.WORDID); //5
				actualProjection = Lib.appendProjection(actualProjection, String.format("GROUP_CONCAT(%s.%s, ', ' ) AS %s", Words.TABLE, Words.WORD, SemRelations_Synsets_Words_X.MEMBERS2));
				groupBy = String.format("%s.%s", C.AS_DEST, Synsets.SYNSETID);
				break;

			case "LEXRELATIONS_SENSES":
				table = String.format("%s AS %s " + //
								"INNER JOIN %s AS %s ON %s.%s = %s.%s " + //
								"INNER JOIN %s AS %s ON %s.%s = %s.%s", //
						LexRelations.TABLE, C.AS_RELATION, //
						Synsets.TABLE, C.AS_DEST, C.AS_RELATION, LexRelations.SYNSET2ID, C.AS_DEST, Synsets.SYNSETID, //
						Words.TABLE, C.AS_WORD, C.AS_RELATION, LexRelations.WORD2ID, C.AS_WORD, Words.WORDID);
				break;

			case "LEXRELATIONS_SENSES_X":
				table = String.format("%s AS %s " + //
								"INNER JOIN %s USING (%s)" + //
								"INNER JOIN %s AS %s ON %s.%s = %s.%s " + //
								"INNER JOIN %s AS %s ON %s.%s = %s.%s ", //
						LexRelations.TABLE, C.AS_RELATION, //
						Relations.TABLE, Relations.RELATIONID, //
						Synsets.TABLE, C.AS_DEST, C.AS_RELATION, LexRelations.SYNSET2ID, C.AS_DEST, Synsets.SYNSETID, //
						Words.TABLE, C.AS_WORD, C.AS_RELATION, LexRelations.WORD2ID, C.AS_WORD, Words.WORDID);
				break;

			case "LEXRELATIONS_SENSES_WORDS_X_BY_SYNSET":
				table = String.format("%s AS %s " + // 1
								"INNER JOIN %s USING (%s) " + // 2
								"INNER JOIN %s AS %s ON %s.%s = %s.%s " + // 3
								"INNER JOIN %s AS %s ON %s.%s = %s.%s " + // 4
								"LEFT JOIN %s AS %s ON %s.%s = %s.%s " + // 5
								"LEFT JOIN %s AS %s USING (%s)", // 6
						LexRelations.TABLE, C.AS_RELATION, // 1
						Relations.TABLE, Relations.RELATIONID, // 2
						Synsets.TABLE, C.AS_DEST, C.AS_RELATION, LexRelations.SYNSET2ID, C.AS_DEST, Synsets.SYNSETID, // 3
						Words.TABLE, C.AS_WORD, C.AS_RELATION, LexRelations.WORD2ID, C.AS_WORD, Words.WORDID, // 4
						Senses.TABLE, C.AS_SENSE, C.AS_DEST, Senses.SYNSETID, C.AS_SENSE, Senses.SYNSETID, // 5
						Words.TABLE, C.AS_WORD2, Words.WORDID); //6
				actualProjection = Lib.appendProjection(actualProjection, String.format("GROUP_CONCAT(DISTINCT %s.%s) AS %s", C.AS_WORD2, Words.WORD, LexRelations_Senses_Words_X.MEMBERS2));
				groupBy = String.format("%s.%s", C.AS_DEST, Synsets.SYNSETID);
				break;

			case "SENSES_VFRAMES":
				table = String.format("%s " + //
								"LEFT JOIN %s USING (%s)", //
						Senses_VFrames.TABLE, //
						VFrames.TABLE, VFrames.FRAMEID);
				break;

			case "SENSES_VTEMPLATES":
				table = String.format("%s " + //
								"LEFT JOIN %s USING (%s)", //
						Senses_VTemplates.TABLE, //
						VTemplates.TABLE, VTemplates.TEMPLATEID);
				break;

			case "SENSES_ADJPOSITIONS":
				table = String.format("%s " + //
								"LEFT JOIN %s USING (%s)", //
						Senses_AdjPositions.TABLE, //
						AdjPositions.TABLE, AdjPositions.POSITIONID);
				break;

			case "LEXES_MORPHS":
				table = String.format("%s " + //
								"LEFT JOIN %s USING (%s)", //
						Lexes_Morphs.TABLE, //
						Morphs.TABLE, Morphs.MORPHID);
				break;

			case "WORDS_LEXES_MORPHS_BY_WORD":
				groupBy = Words.WORDID;
				//$FALL-THROUGH$
				//noinspection fallthrough
			case "WORDS_LEXES_MORPHS":
				table = String.format("%s " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s)", //
						Words.TABLE, //
						Lexes_Morphs.TABLE, Words.WORDID, //
						Morphs.TABLE, Morphs.MORPHID);
				break;

			// T E X T S E A R C H

			case "LOOKUP_FTS_WORDS":
				table = String.format("%s_%s_fts4", Words.TABLE, Words.WORD);
				break;

			case "LOOKUP_FTS_DEFINITIONS":
				table = String.format("%s_%s_fts4", Synsets.TABLE, Synsets.DEFINITION);
				break;

			case "LOOKUP_FTS_SAMPLES":
				table = String.format("%s_%s_fts4", Samples.TABLE, Samples.SAMPLE);
				break;

			// S U G G E S T

			case "SUGGEST_WORDS":
			{
				table = Words.TABLE;
				actualProjection = new String[]{ //
						String.format("%s AS _id", Words.WORDID), //
						String.format("%s AS %s", Words.WORD, "SearchManager.SUGGEST_COLUMN_TEXT_1"), //
						String.format("%s AS %s", Words.WORD, "SearchManager.SUGGEST_COLUMN_QUERY")};
				actualSelection = String.format("%s LIKE ? || '%%'", Words.WORD);
				actualSelectionArgs = new String[]{last};
				groupBy = null;
				break;
			}

			case "SUGGEST_FTS_WORDS":
			{
				table = String.format("%s_%s_fts4", Words.TABLE, Words.WORD);
				actualProjection = new String[]{ //
						String.format("%s AS _id", Words.WORDID), //
						String.format("%s AS %s", Words.WORD, "SearchManager.SUGGEST_COLUMN_TEXT_1"), //
						String.format("%s AS %s", Words.WORD, "SearchManager.SUGGEST_COLUMN_QUERY")}; //
				actualSelection = String.format("%s MATCH ?", Words.WORD);
				actualSelectionArgs = new String[]{last + '*'};
				groupBy = null;
				break;
			}

			case "SUGGEST_FTS_DEFINITIONS":
			{
				table = String.format("%s_%s_fts4", Synsets.TABLE, Synsets.DEFINITION);
				actualProjection = new String[]{ //
						String.format("%s AS _id", Synsets.SYNSETID), //
						String.format("%s AS %s", Synsets.DEFINITION, "SearchManager.SUGGEST_COLUMN_TEXT_1"), //
						String.format("%s AS %s", Synsets.DEFINITION, "SearchManager.SUGGEST_COLUMN_QUERY")};
				actualSelection = String.format("%s MATCH ?", Synsets.DEFINITION);
				actualSelectionArgs = new String[]{last + '*'};
				groupBy = null;
				break;
			}

			case "SUGGEST_FTS_SAMPLES":
			{
				table = String.format("%s_%s_fts4", Samples.TABLE, Samples.SAMPLE);
				actualProjection = new String[]{ //
						String.format("%s AS _id", Samples.SAMPLEID), //
						String.format("%s AS %s", Samples.SAMPLE, "SearchManager.SUGGEST_COLUMN_TEXT_1"), //
						String.format("%s AS %s", Samples.SAMPLE, "SearchManager.SUGGEST_COLUMN_QUERY")};
				actualSelection = String.format("%s MATCH ?", Samples.SAMPLE);
				actualSelectionArgs = new String[]{last + '*'};
				groupBy = null;
				break;
			}

			default:
				return null;
		}
		return new String[]{ //
				table, //
				Arrays.toString(actualProjection), //
				actualSelection, //
				Arrays.toString(actualSelectionArgs), //
				groupBy};
	}
}
