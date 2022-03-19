/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package org.sqlunet.wn;

import org.sqlbuilder.common.Lib;

import java.util.Arrays;

import org.sqlunet.wn.C.*;
import org.sqlbuilder.common.Q;

public class Q1 implements Q
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
				table = C.Words.TABLE + " AS " + C.AS_WORD + " " + //
						"LEFT JOIN senses AS " + C.AS_SENSE + " USING (wordid) " + //
						"LEFT JOIN "+C.Synsets.TABLE +" AS " + C.AS_SYNSET + " USING (synsetid)";
				break;

			case "WORDS_SENSES_CASEDWORDS_SYNSETS":
				table = C.Words.TABLE + " AS " + C.AS_WORD + " " + //
						"LEFT JOIN " + C.Senses.TABLE + " AS " + C.AS_SENSE + " USING (wordid) " + //
						"LEFT JOIN "+C.CasedWords.TABLE +" AS " + C.AS_CASED + " USING (wordid,casedwordid) " + //
						"LEFT JOIN "+C.Synsets.TABLE +" AS " + C.AS_SYNSET + " USING (synsetid)";
				break;

			case "WORDS_SENSES_CASEDWORDS_SYNSETS_POSTYPES_LEXDOMAINS":
				table = C.Words.TABLE + " AS " + C.AS_WORD + " " + //
						"LEFT JOIN " + C.Senses.TABLE + " AS " + C.AS_SENSE + " USING (wordid) " + //
						"LEFT JOIN "+C.CasedWords.TABLE +" AS " + C.AS_CASED + " USING (wordid,casedwordid) " + //
						"LEFT JOIN "+C.Synsets.TABLE +" AS " + C.AS_SYNSET + " USING (synsetid) " + //
						"LEFT JOIN "+C.Poses.TABLE +" AS " + C.AS_POS + " USING (posid) " + //
						"LEFT JOIN "+C.Domains.TABLE +" AS " + C.AS_DOMAIN + " USING (domainid)";
				break;

			case "SENSES_WORDS":
				table = C.Senses.TABLE + " AS " + C.AS_SENSE + " " + //
						"LEFT JOIN " + C.Words.TABLE + " AS " + C.AS_WORD + " USING (wordid)";
				break;

			case "SENSES_WORDS_BY_SYNSET":
				groupBy = "synsetid";
				table = C.Senses.TABLE + " AS " + C.AS_SENSE + " " + //
						"LEFT JOIN " + C.Words.TABLE + " AS " + C.AS_WORD + " USING (wordid)";
				actualProjection = Lib.appendProjection(actualProjection, "GROUP_CONCAT(" + C.Words.TABLE + ".word, ', ' ) AS " + Senses_Words.MEMBERS);
				break;

			case "SENSES_SYNSETS_POSES_DOMAINS":
				table = C.Senses.TABLE + " AS " + C.AS_SENSE + " " + //
						"INNER JOIN "+C.Synsets.TABLE +" AS " + C.AS_SYNSET + " USING (synsetid) " + //
						"LEFT JOIN "+C.Poses.TABLE +" AS " + C.AS_POS + " USING (posid) " + //
						"LEFT JOIN "+C.Domains.TABLE +" AS " + C.AS_DOMAIN + " USING (domainid)";
				break;

			case "SYNSETS_POSES_DOMAINS":
				table = ""+C.Synsets.TABLE +" AS " + C.AS_SYNSET + " " + //
						"LEFT JOIN "+C.Poses.TABLE +" AS " + C.AS_POS + " USING (posid) " + //
						"LEFT JOIN "+C.Domains.TABLE +" AS " + C.AS_DOMAIN + " USING (domainid)";
				break;

			case "BASERELATIONS_SENSES_WORDS_X_BY_SYNSET":
			{
				groupBy = TARGET_SYNSETID + "," + C.AS_TYPE + ",relation,relationid," + TARGET_WORDID + "," + TARGET_WORD;
				table = "( " + "MAKEQUERY" + " ) AS " + C.AS_RELATION + ' ' + //
						"INNER JOIN " + C.Relations.TABLE + " USING (relationid) " + //
						"INNER JOIN "+C.Synsets.TABLE +" AS " + C.AS_DEST + " ON " + C.AS_RELATION + ".synset2id = " + C.AS_DEST + ".synsetid " + //
						"LEFT JOIN " + C.Senses.TABLE + " ON " + C.AS_DEST + ".synsetid = " + C.Senses.TABLE + ".synsetid " + //
						"LEFT JOIN " + C.Words.TABLE + " AS " + C.AS_WORD + " USING (wordid) " + //
						"LEFT JOIN " + C.Words.TABLE + " AS " + C.AS_WORD2 + " ON " + C.AS_RELATION + ".word2id = " + C.AS_WORD2 + ".wordid";
				actualSelection = null;
			}
			break;

			case "SEMRELATIONS_SYNSETS":
				table = SemRelations.TABLE + " AS " + C.AS_RELATION + ' ' + //
						"INNER JOIN "+C.Synsets.TABLE +" AS " + C.AS_DEST + " ON " + C.AS_RELATION + ".synset2id = " + C.AS_DEST + ".synsetid";
				break;

			case "SEMRELATIONS_SYNSETS_X":
				table = SemRelations.TABLE + " AS " + C.AS_RELATION + ' ' + //
						"INNER JOIN " + C.Relations.TABLE + " USING (relationid)" + //
						"INNER JOIN "+C.Synsets.TABLE +" AS " + C.AS_DEST + " ON " + C.AS_RELATION + ".synset2id = " + C.AS_DEST + ".synsetid ";
				break;

			case "SEMRELATIONS_SYNSETS_WORDS_X_BY_SYNSET":
				groupBy = C.AS_DEST + ".synsetid";
				table = SemRelations.TABLE + " AS " + C.AS_RELATION + ' ' + //
						"INNER JOIN " + C.Relations.TABLE + " USING (relationid) " + //
						"INNER JOIN "+C.Synsets.TABLE +" AS " + C.AS_DEST + " ON " + C.AS_RELATION + ".synset2id = " + C.AS_DEST + ".synsetid " + //
						"LEFT JOIN " + C.Senses.TABLE + " ON " + C.AS_DEST + ".synsetid = " + C.Senses.TABLE + ".synsetid " + //
						"LEFT JOIN " + C.Words.TABLE + " USING (wordid)";
				actualProjection = Lib.appendProjection(actualProjection, "GROUP_CONCAT(" + C.Words.TABLE + ".word, ', ' ) AS " + SemRelations_Synsets_Words_X.MEMBERS2);
				break;

			case "LEXRELATIONS_SENSES":
				table = LexRelations.TABLE + " AS " + C.AS_RELATION + ' ' + //
						"INNER JOIN "+C.Synsets.TABLE +" AS " + C.AS_DEST + " ON " + C.AS_RELATION + ".synset2id = " + C.AS_DEST + ".synsetid " + //
						"INNER JOIN " + C.Words.TABLE + " AS " + C.AS_WORD + " ON " + C.AS_RELATION + ".word2id = " + C.AS_WORD + ".wordid";
				break;

			case "LEXRELATIONS_SENSES_X":
				table = LexRelations.TABLE + " AS " + C.AS_RELATION + ' ' + //
						"INNER JOIN " + C.Relations.TABLE + " USING (relationid)" + //
						"INNER JOIN "+C.Synsets.TABLE +" AS " + C.AS_DEST + " ON " + C.AS_RELATION + ".synset2id = " + C.AS_DEST + ".synsetid " + //
						"INNER JOIN " + C.Words.TABLE + " AS " + C.AS_WORD + " ON " + C.AS_RELATION + ".word2id = " + C.AS_WORD + ".wordid ";
				break;

			case "LEXRELATIONS_SENSES_WORDS_X_BY_SYNSET":
				groupBy = C.AS_DEST + ".synsetid";
				actualProjection = Lib.appendProjection(actualProjection, "GROUP_CONCAT(DISTINCT " + C.AS_WORD2 + ".word) AS " + LexRelations_Senses_Words_X.MEMBERS2);
				table = LexRelations.TABLE + " AS " + C.AS_RELATION + ' ' + //
						"INNER JOIN " + C.Relations.TABLE + " USING (relationid) " + //
						"INNER JOIN "+C.Synsets.TABLE +" AS " + C.AS_DEST + " ON " + C.AS_RELATION + ".synset2id = " + C.AS_DEST + ".synsetid " + //
						"INNER JOIN " + C.Words.TABLE + " AS " + C.AS_WORD + " ON " + C.AS_RELATION + ".word2id = " + C.AS_WORD + ".wordid " + //
						"LEFT JOIN " + C.Senses.TABLE + " AS " + C.AS_SENSE + " ON " + C.AS_DEST + ".synsetid = " + C.AS_SENSE + ".synsetid " + //
						"LEFT JOIN " + C.Words.TABLE + " AS " + C.AS_WORD2 + " USING (wordid)";
				break;

			case "SENSES_VFRAMES":
				table = Senses_VFrames.TABLE + " " + //
						"LEFT JOIN vframes USING (frameid)";
				break;

			case "SENSES_VTEMPLATES":
				table = Senses_VTemplates.TABLE + " " + //
						"LEFT JOIN vtemplates USING (templateid)";
				break;

			case "SENSES_ADJPOSITIONS":
				table = "senses_adjpositions " + //
						"LEFT JOIN adjpositions USING (positionid)";
				break;

			case "LEXES_MORPHS":
				table = Lexes_Morphs.TABLE + " " + //
						"LEFT JOIN morphs USING (morphid)";
				break;

			case "WORDS_LEXES_MORPHS_BY_WORD":
				groupBy = "wordid";
				//$FALL-THROUGH$
				//noinspection fallthrough
			case "WORDS_LEXES_MORPHS":
				table = C.Words.TABLE + " " + //
						"LEFT JOIN lexes_morphs USING (wordid) " + //
						"LEFT JOIN morphs USING (morphid)";
				break;

			// T E X T S E A R C H

			case "LOOKUP_FTS_WORDS":
				table = C.Words.TABLE + "_word_fts4";
				break;

			case "LOOKUP_FTS_DEFINITIONS":
				table = ""+C.Synsets.TABLE +"_definition_fts4";
				break;

			case "LOOKUP_FTS_SAMPLES":
				table = "samples_sample_fts4";
				break;

			// S U G G E S T

			case "SUGGEST_WORDS":
			{
				table = C.Words.TABLE + "";
				actualProjection = new String[]{"wordid AS _id", //
						"word AS " + "SearchManager.SUGGEST_COLUMN_TEXT_1", //
						"word AS " + "SearchManager.SUGGEST_COLUMN_QUERY"};
				actualSelection = "word LIKE ? || '%'";
				actualSelectionArgs = new String[]{last};
				groupBy = null;
				break;
			}

			case "SUGGEST_FTS_WORDS":
			{
				table = C.Words.TABLE + "_word_fts4";
				actualProjection = new String[]{"wordid AS _id", //
						"word AS " + "SearchManager.SUGGEST_COLUMN_TEXT_1", //
						"word AS " + "SearchManager.SUGGEST_COLUMN_QUERY"}; //
				actualSelection = "word MATCH ?";
				actualSelectionArgs = new String[]{last + '*'};
				groupBy = null;
				break;
			}

			case "SUGGEST_FTS_DEFINITIONS":
			{
				table = ""+C.Synsets.TABLE +"_definition_fts4";
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
		return new String[]{ //
				table, //
				Arrays.toString(actualProjection), //
				actualSelection, //
				Arrays.toString(actualSelectionArgs), //
				groupBy
		};
	}
}
