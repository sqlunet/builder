/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package provider;

import java.util.Arrays;

import provider.C.*;

public class Q2 implements Q
{
	//static private final String SOURCE_SYNSETID = "s_synsetid";
	//static private final String SOURCE_DEFINITION = "s_definition";
	//static private final String SOURCE_WORD = "s_word";
	//static private final String SOURCE_WORDID = "s_wordid";
	static public final String TARGET_SYNSETID = "d_synsetid";
	static public final String TARGET_DEFINITION = "d_definition";
	static public final String TARGET_LEMMA = "d_lemma";
	static public final String TARGET_WORDID = "d_wordid";

	@Override
	public String[] query(int code)
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

		switch (code)
		{
			// T A B L E
			// table uri : last element is table

			case Main.WORDS:
				table = "URI_PATH_SEGMENT";
				break;
			case Main.SENSES:
				table = "URI_PATH_SEGMENT";
				break;
			case Main.SYNSETS:
				table = "URI_PATH_SEGMENT";
				break;
			case Main.SEMRELATIONS:
				table = "URI_PATH_SEGMENT";
				break;
			case Main.LEXRELATIONS:
				table = "URI_PATH_SEGMENT";
				break;
			case Main.RELATIONS:
				table = "URI_PATH_SEGMENT";
				break;
			case Main.POSES:
				table = "URI_PATH_SEGMENT";
				break;
			case Main.DOMAINS:
				table = "URI_PATH_SEGMENT";
				break;
			case Main.ADJPOSITIONS:
				table = "URI_PATH_SEGMENT";
				break;
			case Main.SAMPLES:
				table = "URI_PATH_SEGMENT";
				break;

			// I T E M
			// the incoming URI was for a single item because this URI was for a single row, the _ID value part is present.
			// get the last path segment from the URI: this is the _ID value. then, append the value to the WHERE clause for the query

			case Main.WORD:
				table = Words.TABLE;
				break;

			case Main.SENSE:
				table = Senses.TABLE;
				break;

			case Main.SYNSET:
				table = Synsets.TABLE;
				break;

			// V I E W S

			case Main.DICT:
				table = "URI_PATH_SEGMENT";
				break;

			// J O I N S

			case Main.WORDS_SENSES_SYNSETS:
				table = String.format("%s AS %s " + //
								"LEFT JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s USING (%s)", //
						Words.TABLE, C.AS_WORD, //
						Senses.TABLE, C.AS_SENSE, Senses.WORDID, //
						Synsets.TABLE, C.AS_SYNSET, Synsets.SYNSETID);
				break;

			case Main.WORDS_SENSES_CASEDWORDS_SYNSETS:
				table = String.format("%s AS %s " + //
								"LEFT JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s USING (%s,%s) " + //
								"LEFT JOIN %s AS %s USING (%s)", //
						Words.TABLE, C.AS_WORD, //
						Senses.TABLE, C.AS_SENSE, Senses.WORDID, //
						CasedWords.TABLE, C.AS_CASED, CasedWords.WORDID, CasedWords.CASEDWORDID, //
						Synsets.TABLE, C.AS_SYNSET, Synsets.SYNSETID);
				break;

			case Main.WORDS_SENSES_CASEDWORDS_SYNSETS_POSTYPES_LEXDOMAINS:
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

			case Main.SENSES_WORDS:
				table = String.format("%s AS %s " + //
								"LEFT JOIN %s AS %s USING(%s)", //
						Senses.TABLE, C.AS_SENSE, //
						Words.TABLE, C.AS_WORD, Senses.WORDID);
				break;

			case Main.SENSES_WORDS_BY_SYNSET:
				groupBy = Synsets.SYNSETID;
				table = String.format("%s AS %s " + //
								"LEFT JOIN %s AS %s USING(%s)", //
						Senses.TABLE, C.AS_SENSE, //
						Words.TABLE, C.AS_WORD, Words.WORDID);
				actualProjection = Main.appendProjection(actualProjection, String.format("GROUP_CONCAT(%s.%s, ', ' ) AS %s", Words.TABLE, Words.WORD, Senses_Words.MEMBERS));
				break;

			case Main.SENSES_SYNSETS_POSES_DOMAINS:
				table = String.format("%s AS %s " + //
								"INNER JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s USING(%s) " + //
								"LEFT JOIN %s AS %s USING(%s)", //
						Senses.TABLE, C.AS_SENSE, //
						Synsets.TABLE, C.AS_SYNSET, Synsets.SYNSETID, //
						Poses.TABLE, C.AS_POS, Poses.POSID, //
						Domains.TABLE, C.AS_DOMAIN, Domains.DOMAINID);
				break;

			case Main.SYNSETS_POSES_DOMAINS:
				table = String.format("%s AS %s " + //
								"LEFT JOIN %s AS %s USING(%s) " + //
								"LEFT JOIN %s AS %s USING(%s)", //
						Synsets.TABLE, C.AS_SYNSET, //
						Poses.TABLE, C.AS_POS, Poses.POSID,//
						Domains.TABLE, C.AS_DOMAIN, Domains.DOMAINID);
				break;

			case Main.BASERELATIONS_SENSES_WORDS_X_BY_SYNSET:
			{
				groupBy = String.format("%s,%s,%s,%s,%s,%s", TARGET_SYNSETID, C.AS_TYPE, Relations.RELATION, Relations.RELATIONID, TARGET_WORDID, TARGET_LEMMA);
				table = String.format("( %s ) AS %s " + // 1
								"INNER JOIN %s USING (%s) " + // 2
								"INNER JOIN %s AS %s ON %s.%s = %s.%s " + // 3
								"LEFT JOIN %s ON %s.%s = %s.%s " + // 4
								"LEFT JOIN %s AS %s USING (%s) " + // 5
								"LEFT JOIN %s AS %s ON %s.%s = %s.%s", // 6
						"MAKEQUERY", C.AS_RELATION, // 1
						Relations.TABLE, Relations.RELATIONID, // 2
						Synsets.TABLE, C.AS_DEST, C.AS_RELATION, BaseRelations.SYNSETID2, C.AS_DEST, Synsets.SYNSETID, // 3
						Senses.TABLE, C.AS_DEST, Synsets.SYNSETID, Senses.TABLE, Senses.SYNSETID, // 4
						Words.TABLE, C.AS_WORD, Words.WORDID, //
						Words.TABLE, C.AS_WORD2, C.AS_RELATION, BaseRelations.WORDID2, C.AS_WORD2, Words.WORDID);
				actualSelection = null;
			}
			break;

			case Main.SEMRELATIONS_SYNSETS:
				table = String.format("%s AS %s " + //
								"INNER JOIN %s AS %s ON %s.%s = %s.%s", SemRelations.TABLE, C.AS_RELATION, //
						Synsets.TABLE, C.AS_DEST, C.AS_RELATION, SemRelations.SYNSETID2, C.AS_DEST, Synsets.SYNSETID);
				break;

			case Main.SEMRELATIONS_SYNSETS_X:
				table = String.format("%s AS %s " + //
								"INNER JOIN %s USING (%s)" + //
								"INNER JOIN %s AS %s ON %s.%s = %s.%s ", //
						SemRelations.TABLE, C.AS_RELATION, //
						Relations.TABLE, Relations.RELATIONID, //
						Synsets.TABLE, C.AS_DEST, C.AS_RELATION, SemRelations.SYNSETID2, C.AS_DEST, Synsets.SYNSETID);
				break;

			case Main.SEMRELATIONS_SYNSETS_WORDS_X_BY_SYNSET:
				groupBy = C.AS_DEST + ".synsetid";
				table = String.format("%s AS %s " + // 1
								"INNER JOIN %s USING (%s) " + // 2
								"INNER JOIN %s AS %s ON %s.%s = %s.%s " + // 3
								"LEFT JOIN %s ON %s.%s = %s.%s " + // 4
								"LEFT JOIN %s USING (%s)", // 5
						SemRelations.TABLE, C.AS_RELATION, // 1
						Relations.TABLE, Relations.RELATIONID, // 2
						Synsets.TABLE, C.AS_DEST, C.AS_RELATION, SemRelations.SYNSETID2, C.AS_DEST, Synsets.SYNSETID, // 3
						Senses.TABLE, C.AS_DEST, Synsets.SYNSETID, Senses.TABLE, Senses.SYNSETID, // 4
						Words.TABLE, Words.WORDID); //5
				actualProjection = Main.appendProjection(actualProjection, "GROUP_CONCAT(" + Words.TABLE + ".lemma, ', ' ) AS " + SemRelations_Synsets_Words_X.MEMBERS2);
				break;

			case Main.LEXRELATIONS_SENSES:
				table = LexRelations.TABLE + " AS " + C.AS_RELATION + ' ' + //
						"INNER JOIN " + Synsets.TABLE + " AS " + C.AS_DEST + " ON " + C.AS_RELATION + ".synset2id = " + C.AS_DEST + ".synsetid " + //
						"INNER JOIN " + Words.TABLE + " AS " + C.AS_WORD + " ON " + C.AS_RELATION + ".word2id = " + C.AS_WORD + ".wordid";
				break;

			case Main.LEXRELATIONS_SENSES_X:
				table = LexRelations.TABLE + " AS " + C.AS_RELATION + ' ' + //
						"INNER JOIN " + Relations.TABLE + " USING (linkid)" + //
						"INNER JOIN " + Synsets.TABLE + " AS " + C.AS_DEST + " ON " + C.AS_RELATION + ".synset2id = " + C.AS_DEST + ".synsetid " + //
						"INNER JOIN " + Words.TABLE + " AS " + C.AS_WORD + " ON " + C.AS_RELATION + ".word2id = " + C.AS_WORD + ".wordid ";
				break;

			case Main.LEXRELATIONS_SENSES_WORDS_X_BY_SYNSET:
				groupBy = C.AS_DEST + ".synsetid";
				actualProjection = Main.appendProjection(actualProjection, "GROUP_CONCAT(DISTINCT " + C.AS_WORD2 + ".lemma) AS " + LexRelations_Senses_Words_X.MEMBERS2);
				table = LexRelations.TABLE + " AS " + C.AS_RELATION + ' ' + //
						"INNER JOIN " + Relations.TABLE + " USING (linkid) " + //
						"INNER JOIN " + Synsets.TABLE + " AS " + C.AS_DEST + " ON " + C.AS_RELATION + ".synset2id = " + C.AS_DEST + ".synsetid " + //
						"INNER JOIN " + Words.TABLE + " AS " + C.AS_WORD + " ON " + C.AS_RELATION + ".word2id = " + C.AS_WORD + ".wordid " + //
						"LEFT JOIN " + Senses.TABLE + " AS " + C.AS_SENSE + " ON " + C.AS_DEST + ".synsetid = " + C.AS_SENSE + ".synsetid " + //
						"LEFT JOIN " + Words.TABLE + " AS " + C.AS_WORD2 + " USING (wordid)";
				break;

			case Main.SENSES_VFRAMES:
				table = Senses_VerbFrames.TABLE + " " + //
						"LEFT JOIN vframes USING (frameid)";
				break;

			case Main.SENSES_VTEMPLATES:
				table = Senses_VerbTemplates.TABLE + " " + //
						"LEFT JOIN vframesentences USING (sentenceid)";
				break;

			case Main.SENSES_ADJPOSITIONS:
				table = "adjpositions " + //
						"LEFT JOIN adjpositiontypes USING (position)";
				break;

			case Main.LEXES_MORPHS:
				table = Lexes_Morphs.TABLE + " " + //
						"LEFT JOIN morphs USING (morphid)";
				break;

			case Main.WORDS_LEXES_MORPHS_BY_WORD:
				groupBy = "wordid";
				//$FALL-THROUGH$
				//noinspection fallthrough
			case Main.WORDS_LEXES_MORPHS:
				table = Words.TABLE + " " + //
						"LEFT JOIN morphmaps USING (wordid) " + //
						"LEFT JOIN morphs USING (morphid)";
				break;

			// T E X T S E A R C H

			case Main.LOOKUP_FTS_WORDS:
				table = Words.TABLE + "_lemma_fts4";
				break;

			case Main.LOOKUP_FTS_DEFINITIONS:
				table = Synsets.TABLE + "_definition_fts4";
				break;

			case Main.LOOKUP_FTS_SAMPLES:
				table = "samples_sample_fts4";
				break;

			// S U G G E S T

			case Main.SUGGEST_WORDS:
			{
				table = Words.TABLE + "";
				actualProjection = new String[]{"wordid AS _id", //
						"lemma AS " + "SearchManager.SUGGEST_COLUMN_TEXT_1", //
						"lemma AS " + "SearchManager.SUGGEST_COLUMN_QUERY"};
				actualSelection = "lemma LIKE ? || '%'";
				actualSelectionArgs = new String[]{last};
				groupBy = null;
				break;
			}

			case Main.SUGGEST_FTS_WORDS:
			{
				table = Words.TABLE + "_lemma_fts4";
				actualProjection = new String[]{"wordid AS _id", //
						"lemma AS " + "SearchManager.SUGGEST_COLUMN_TEXT_1", //
						"lemma AS " + "SearchManager.SUGGEST_COLUMN_QUERY"}; //
				actualSelection = "lemma MATCH ?";
				actualSelectionArgs = new String[]{last + '*'};
				groupBy = null;
				break;
			}

			case Main.SUGGEST_FTS_DEFINITIONS:
			{
				table = Synsets.TABLE + "_definition_fts4";
				actualProjection = new String[]{"synsetid AS _id", //
						"definition AS " + "SearchManager.SUGGEST_COLUMN_TEXT_1", //
						"definition AS " + "SearchManager.SUGGEST_COLUMN_QUERY"};
				actualSelection = "definition MATCH ?";
				actualSelectionArgs = new String[]{last + '*'};
				groupBy = null;
				break;
			}

			case Main.SUGGEST_FTS_SAMPLES:
			{
				table = "samples_sample_fts4";
				actualProjection = new String[]{"sampleid AS _id", //
						"sample AS " + "SearchManager.SUGGEST_COLUMN_TEXT_1", //
						"sample AS " + "SearchManager.SUGGEST_COLUMN_QUERY"};
				actualSelection = "sample MATCH ?";
				actualSelectionArgs = new String[]{last + '*'};
				groupBy = null;
				break;
			}

			default:
				return null;
		}
		return new String[]{"T:" + table, //
				"P:" + Arrays.toString(actualProjection), //
				"S:" + actualSelection, //
				"A:" + Arrays.toString(actualSelectionArgs), //
				"G:" + groupBy};
	}
}
