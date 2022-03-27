/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package org.sqlunet.wn;

import org.sqlbuilder.common.Lib;

import java.util.Arrays;

import org.sqlunet.wn.C.*;
import org.sqlbuilder.common.Q;

public class Q0 implements Q
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
			case "SENSES":
			case "SYNSETS":
			case "SEMRELATIONS":
			case "LEXRELATIONS":
			case "RELATIONS":
			case "POSES":
			case "DOMAINS":
			case "ADJPOSITIONS":
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
				table = "words AS " + C.AS_WORD + " " + //
						"LEFT JOIN senses AS " + C.AS_SENSE + " USING (wordid) " + //
						"LEFT JOIN synsets AS " + C.AS_SYNSET + " USING (synsetid)";
				break;

			case "WORDS_SENSES_CASEDWORDS_SYNSETS":
				table = "words AS " + C.AS_WORD + " " + //
						"LEFT JOIN senses AS " + C.AS_SENSE + " USING (wordid) " + //
						"LEFT JOIN casedwords AS " + C.AS_CASED + " USING (wordid,casedwordid) " + //
						"LEFT JOIN synsets AS " + C.AS_SYNSET + " USING (synsetid)";
				break;

			case "WORDS_SENSES_CASEDWORDS_SYNSETS_POSTYPES_LEXDOMAINS":
				table = "words AS " + C.AS_WORD + " " + //
						"LEFT JOIN senses AS " + C.AS_SENSE + " USING (wordid) " + //
						"LEFT JOIN casedwords AS " + C.AS_CASED + " USING (wordid,casedwordid) " + //
						"LEFT JOIN synsets AS " + C.AS_SYNSET + " USING (synsetid) " + //
						"LEFT JOIN postypes AS " + C.AS_POS + " USING (pos) " + //
						"LEFT JOIN lexdomains AS " + C.AS_DOMAIN + " USING (lexdomainid)";
				break;

			case "SENSES_WORDS":
				table = "senses AS " + C.AS_SENSE + " " + //
						"LEFT JOIN words AS " + C.AS_WORD + " USING(wordid)";
				break;

			case "SENSES_WORDS_BY_SYNSET":
				groupBy = "synsetid";
				table = "senses AS " + C.AS_SENSE + " " + //
						"LEFT JOIN words AS " + C.AS_WORD + " USING(wordid)";
				actualProjection = Lib.appendProjection(actualProjection, "GROUP_CONCAT(words.lemma, ', ' ) AS " + Senses_Words.MEMBERS);
				break;

			case "SENSES_SYNSETS_POSES_DOMAINS":
				table = "senses AS " + C.AS_SENSE + " " + //
						"INNER JOIN synsets AS " + C.AS_SYNSET + " USING (synsetid) " + //
						"LEFT JOIN postypes AS " + C.AS_POS + " USING(pos) " + //
						"LEFT JOIN lexdomains AS " + C.AS_DOMAIN + " USING(lexdomainid)";
				break;

			case "SYNSETS_POSES_DOMAINS":
				table = "synsets AS " + C.AS_SYNSET + " " + //
						"LEFT JOIN postypes AS " + C.AS_POS + " USING(pos) " + //
						"LEFT JOIN lexdomains AS " + C.AS_DOMAIN + " USING(lexdomainid)";
				break;

			case "ALLRELATIONS_SENSES_WORDS_X_BY_SYNSET":
			{
				final String table1 = "semlinks";
				final String table2 = "lexlinks";
				final String[] projection1 = { //
						SemRelations.RELATIONID, //
						SemRelations.SYNSET1ID, //
						SemRelations.SYNSET2ID, //
				};
				final String[] projection2 = { //
						LexRelations.RELATIONID, //
						LexRelations.WORD1ID, //
						LexRelations.SYNSET1ID, //
						LexRelations.WORD2ID, //
						LexRelations.SYNSET2ID, //
				};
				final String[] unionProjection = { //
						AllRelations.RELATIONID, //
						AllRelations.WORD1ID, //
						AllRelations.SYNSET1ID, //
						AllRelations.WORD2ID, //
						AllRelations.SYNSET2ID, //
				};
				groupBy = TARGET_SYNSETID + " , " + C.AS_TYPE + " , link, linkid, " + TARGET_WORDID + ',' + TARGET_LEMMA;
				table = "( " + "MAKEQUERY" + " ) AS " + C.AS_RELATION + ' ' + //
						"INNER JOIN linktypes USING (linkid) " + //
						"INNER JOIN synsets AS " + C.AS_DEST + " ON " + C.AS_RELATION + ".synset2id = " + C.AS_DEST + ".synsetid " + //
						"LEFT JOIN senses ON " + C.AS_DEST + ".synsetid = senses.synsetid " + //
						"LEFT JOIN words AS " + C.AS_WORD + " USING (wordid) " + //
						"LEFT JOIN words AS " + C.AS_WORD2 + " ON " + C.AS_RELATION + ".word2id = " + C.AS_WORD2 + ".wordid";
				actualSelection = null;
			}
			break;

			case "SEMRELATIONS_SYNSETS":
				table = "semlinks AS " + C.AS_RELATION + ' ' + //
						"INNER JOIN synsets AS " + C.AS_DEST + " ON " + C.AS_RELATION + ".synset2id = " + C.AS_DEST + ".synsetid";
				break;

			case "SEMRELATIONS_SYNSETS_X":
				table = "semlinks AS " + C.AS_RELATION + ' ' + //
						"INNER JOIN linktypes USING (linkid)" + //
						"INNER JOIN synsets AS " + C.AS_DEST + " ON " + C.AS_RELATION + ".synset2id = " + C.AS_DEST + ".synsetid ";
				break;

			case "SEMRELATIONS_SYNSETS_WORDS_X_BY_SYNSET":
				groupBy = C.AS_DEST + ".synsetid";
				table = "semlinks AS " + C.AS_RELATION + ' ' + //
						"INNER JOIN linktypes USING (linkid) " + //
						"INNER JOIN synsets AS " + C.AS_DEST + " ON " + C.AS_RELATION + ".synset2id = " + C.AS_DEST + ".synsetid " + //
						"LEFT JOIN senses ON " + C.AS_DEST + ".synsetid = senses.synsetid " + //
						"LEFT JOIN words USING (wordid)";
				actualProjection = Lib.appendProjection(actualProjection, "GROUP_CONCAT(words.lemma, ', ' ) AS " + SemRelations_Synsets_Words_X.MEMBERS2);
				break;

			case "LEXRELATIONS_SENSES":
				table = "lexlinks AS " + C.AS_RELATION + ' ' + //
						"INNER JOIN synsets AS " + C.AS_DEST + " ON " + C.AS_RELATION + ".synset2id = " + C.AS_DEST + ".synsetid " + //
						"INNER JOIN words AS " + C.AS_WORD + " ON " + C.AS_RELATION + ".word2id = " + C.AS_WORD + ".wordid";
				break;

			case "LEXRELATIONS_SENSES_X":
				table = "lexlinks AS " + C.AS_RELATION + ' ' + //
						"INNER JOIN linktypes USING (linkid)" + //
						"INNER JOIN synsets AS " + C.AS_DEST + " ON " + C.AS_RELATION + ".synset2id = " + C.AS_DEST + ".synsetid " + //
						"INNER JOIN words AS " + C.AS_WORD + " ON " + C.AS_RELATION + ".word2id = " + C.AS_WORD + ".wordid ";
				break;

			case "LEXRELATIONS_SENSES_WORDS_X_BY_SYNSET":
				groupBy = C.AS_DEST + ".synsetid";
				actualProjection = Lib.appendProjection(actualProjection, "GROUP_CONCAT(DISTINCT " + C.AS_WORD2 + ".lemma) AS " + LexRelations_Senses_Words_X.MEMBERS2);
				table = "lexlinks AS " + C.AS_RELATION + ' ' + //
						"INNER JOIN linktypes USING (linkid) " + //
						"INNER JOIN synsets AS " + C.AS_DEST + " ON " + C.AS_RELATION + ".synset2id = " + C.AS_DEST + ".synsetid " + //
						"INNER JOIN words AS " + C.AS_WORD + " ON " + C.AS_RELATION + ".word2id = " + C.AS_WORD + ".wordid " + //
						"LEFT JOIN senses AS " + C.AS_SENSE + " ON " + C.AS_DEST + ".synsetid = " + C.AS_SENSE + ".synsetid " + //
						"LEFT JOIN words AS " + C.AS_WORD2 + " USING (wordid)";
				break;

			case "SENSES_VFRAMES":
				table = "senses_vframes " + //
						"LEFT JOIN vframes USING (frameid)";
				break;

			case "SENSES_VTEMPLATES":
				table = "vframesentencemaps " + //
						"LEFT JOIN vframesentences USING (sentenceid)";
				break;

			case "SENSES_ADJPOSITIONS":
				table = "adjpositions " + //
						"LEFT JOIN adjpositiontypes USING (position)";
				break;

			case "LEXES_MORPHS":
				table = "morphmaps " + //
						"LEFT JOIN morphs USING (morphid)";
				break;

			case "WORDS_LEXES_MORPHS_BY_WORD":
				groupBy = "wordid";
				//$FALL-THROUGH$
				//noinspection fallthrough
			case "WORDS_LEXES_MORPHS":
				table = "words " + //
						"LEFT JOIN morphmaps USING (wordid) " + //
						"LEFT JOIN morphs USING (morphid)";
				break;

			// T E X T S E A R C H

			case "LOOKUP_FTS_WORDS":
				table = "words_lemma_fts4";
				break;

			case "LOOKUP_FTS_DEFINITIONS":
				table = "synsets_definition_fts4";
				break;

			case "LOOKUP_FTS_SAMPLES":
				table = "samples_sample_fts4";
				break;

			// S U G G E S T

			case "SUGGEST_WORDS":
			{
				table = "words";
				actualProjection = new String[]{"wordid AS _id", //
						"lemma AS " + "SearchManager.SUGGEST_COLUMN_TEXT_1", //
						"lemma AS " + "SearchManager.SUGGEST_COLUMN_QUERY"};
				actualSelection = "lemma LIKE ? || '%'";
				actualSelectionArgs = new String[]{last};
				groupBy = null;
				break;
			}

			case "SUGGEST_FTS_WORDS":
			{
				table = "words_lemma_fts4";
				actualProjection = new String[]{"wordid AS _id", //
						"lemma AS " + "SearchManager.SUGGEST_COLUMN_TEXT_1", //
						"lemma AS " + "SearchManager.SUGGEST_COLUMN_QUERY"}; //
				actualSelection = "lemma MATCH ?";
				actualSelectionArgs = new String[]{last + '*'};
				groupBy = null;
				break;
			}

			case "SUGGEST_FTS_DEFINITIONS":
			{
				table = "synsets_definition_fts4";
				actualProjection = new String[]{"synsetid AS _id", //
						"definition AS " + "SearchManager.SUGGEST_COLUMN_TEXT_1", //
						"definition AS " + "SearchManager.SUGGEST_COLUMN_QUERY"};
				actualSelection = "definition MATCH ?";
				actualSelectionArgs = new String[]{last + '*'};
				groupBy = null;
				break;
			}

			case "SUGGEST_FTS_SAMPLES":
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
		return new String[]{
				table, //
				Arrays.toString(actualProjection), //
				actualSelection, //
				Arrays.toString(actualSelectionArgs), //
				groupBy
		};
	}
}
