/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package provider;

import java.util.Arrays;

import provider.C.*;

public class Q1 implements Q
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
			case Main.SENSES:
			case Main.SYNSETS:
			case Main.SEMRELATIONS:
			case Main.LEXRELATIONS:
			case Main.RELATIONS:
			case Main.POSES:
			case Main.DOMAINS:
			case Main.ADJPOSITIONS:
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
				table = C.Words.TABLE + " AS " + C.AS_WORD + " " + //
						"LEFT JOIN senses AS " + C.AS_SENSE + " USING (wordid) " + //
						"LEFT JOIN "+C.Synsets.TABLE +" AS " + C.AS_SYNSET + " USING (synsetid)";
				break;

			case Main.WORDS_SENSES_CASEDWORDS_SYNSETS:
				table = C.Words.TABLE + " AS " + C.AS_WORD + " " + //
						"LEFT JOIN " + C.Senses.TABLE + " AS " + C.AS_SENSE + " USING (wordid) " + //
						"LEFT JOIN "+C.CasedWords.TABLE +" AS " + C.AS_CASED + " USING (wordid,casedwordid) " + //
						"LEFT JOIN "+C.Synsets.TABLE +" AS " + C.AS_SYNSET + " USING (synsetid)";
				break;

			case Main.WORDS_SENSES_CASEDWORDS_SYNSETS_POSTYPES_LEXDOMAINS:
				table = C.Words.TABLE + " AS " + C.AS_WORD + " " + //
						"LEFT JOIN " + C.Senses.TABLE + " AS " + C.AS_SENSE + " USING (wordid) " + //
						"LEFT JOIN "+C.CasedWords.TABLE +" AS " + C.AS_CASED + " USING (wordid,casedwordid) " + //
						"LEFT JOIN "+C.Synsets.TABLE +" AS " + C.AS_SYNSET + " USING (synsetid) " + //
						"LEFT JOIN "+C.Poses.TABLE +" AS " + C.AS_POS + " USING (posid) " + //
						"LEFT JOIN "+C.Domains.TABLE +" AS " + C.AS_DOMAIN + " USING (domainid)";
				break;

			case Main.SENSES_WORDS:
				table = C.Senses.TABLE + " AS " + C.AS_SENSE + " " + //
						"LEFT JOIN " + C.Words.TABLE + " AS " + C.AS_WORD + " USING(wordid)";
				break;

			case Main.SENSES_WORDS_BY_SYNSET:
				groupBy = "synsetid";
				table = C.Senses.TABLE + " AS " + C.AS_SENSE + " " + //
						"LEFT JOIN " + C.Words.TABLE + " AS " + C.AS_WORD + " USING(wordid)";
				actualProjection = Main.appendProjection(actualProjection, "GROUP_CONCAT(" + C.Words.TABLE + ".word, ', ' ) AS " + Senses_Words.MEMBERS);
				break;

			case Main.SENSES_SYNSETS_POSES_DOMAINS:
				table = C.Senses.TABLE + " AS " + C.AS_SENSE + " " + //
						"INNER JOIN "+C.Synsets.TABLE +" AS " + C.AS_SYNSET + " USING (synsetid) " + //
						"LEFT JOIN "+C.Poses.TABLE +" AS " + C.AS_POS + " USING(posid) " + //
						"LEFT JOIN "+C.Domains.TABLE +" AS " + C.AS_DOMAIN + " USING(domainid)";
				break;

			case Main.SYNSETS_POSES_DOMAINS:
				table = ""+C.Synsets.TABLE +" AS " + C.AS_SYNSET + " " + //
						"LEFT JOIN "+C.Poses.TABLE +" AS " + C.AS_POS + " USING(posid) " + //
						"LEFT JOIN "+C.Domains.TABLE +" AS " + C.AS_DOMAIN + " USING(domainid)";
				break;

			case Main.BASERELATIONS_SENSES_WORDS_X_BY_SYNSET:
			{
				groupBy = TARGET_SYNSETID + "," + C.AS_TYPE + ",relation,relationid," + TARGET_WORDID + "," + TARGET_LEMMA;
				table = "( " + "MAKEQUERY" + " ) AS " + C.AS_RELATION + ' ' + //
						"INNER JOIN " + C.Relations.TABLE + " USING (relationid) " + //
						"INNER JOIN "+C.Synsets.TABLE +" AS " + C.AS_DEST + " ON " + C.AS_RELATION + ".synset2id = " + C.AS_DEST + ".synsetid " + //
						"LEFT JOIN " + C.Senses.TABLE + " ON " + C.AS_DEST + ".synsetid = " + C.Senses.TABLE + ".synsetid " + //
						"LEFT JOIN " + C.Words.TABLE + " AS " + C.AS_WORD + " USING (wordid) " + //
						"LEFT JOIN " + C.Words.TABLE + " AS " + C.AS_WORD2 + " ON " + C.AS_RELATION + ".word2id = " + C.AS_WORD2 + ".wordid";
				actualSelection = null;
			}
			break;

			case Main.SEMRELATIONS_SYNSETS:
				table = C.SemRelations.TABLE + " AS " + C.AS_RELATION + ' ' + //
						"INNER JOIN "+C.Synsets.TABLE +" AS " + C.AS_DEST + " ON " + C.AS_RELATION + ".synset2id = " + C.AS_DEST + ".synsetid";
				break;

			case Main.SEMRELATIONS_SYNSETS_X:
				table = C.SemRelations.TABLE + " AS " + C.AS_RELATION + ' ' + //
						"INNER JOIN " + C.Relations.TABLE + " USING (relationid)" + //
						"INNER JOIN "+C.Synsets.TABLE +" AS " + C.AS_DEST + " ON " + C.AS_RELATION + ".synset2id = " + C.AS_DEST + ".synsetid ";
				break;

			case Main.SEMRELATIONS_SYNSETS_WORDS_X_BY_SYNSET:
				groupBy = C.AS_DEST + ".synsetid";
				table = C.SemRelations.TABLE + " AS " + C.AS_RELATION + ' ' + //
						"INNER JOIN " + C.Relations.TABLE + " USING (relationid) " + //
						"INNER JOIN "+C.Synsets.TABLE +" AS " + C.AS_DEST + " ON " + C.AS_RELATION + ".synset2id = " + C.AS_DEST + ".synsetid " + //
						"LEFT JOIN " + C.Senses.TABLE + " ON " + C.AS_DEST + ".synsetid = " + C.Senses.TABLE + ".synsetid " + //
						"LEFT JOIN " + C.Words.TABLE + " USING (wordid)";
				actualProjection = Main.appendProjection(actualProjection, "GROUP_CONCAT(" + C.Words.TABLE + ".lemma, ', ' ) AS " + SemRelations_Synsets_Words_X.MEMBERS2);
				break;

			case Main.LEXRELATIONS_SENSES:
				table = C.LexRelations.TABLE + " AS " + C.AS_RELATION + ' ' + //
						"INNER JOIN "+C.Synsets.TABLE +" AS " + C.AS_DEST + " ON " + C.AS_RELATION + ".synset2id = " + C.AS_DEST + ".synsetid " + //
						"INNER JOIN " + C.Words.TABLE + " AS " + C.AS_WORD + " ON " + C.AS_RELATION + ".word2id = " + C.AS_WORD + ".wordid";
				break;

			case Main.LEXRELATIONS_SENSES_X:
				table = C.LexRelations.TABLE + " AS " + C.AS_RELATION + ' ' + //
						"INNER JOIN " + C.Relations.TABLE + " USING (linkid)" + //
						"INNER JOIN "+C.Synsets.TABLE +" AS " + C.AS_DEST + " ON " + C.AS_RELATION + ".synset2id = " + C.AS_DEST + ".synsetid " + //
						"INNER JOIN " + C.Words.TABLE + " AS " + C.AS_WORD + " ON " + C.AS_RELATION + ".word2id = " + C.AS_WORD + ".wordid ";
				break;

			case Main.LEXRELATIONS_SENSES_WORDS_X_BY_SYNSET:
				groupBy = C.AS_DEST + ".synsetid";
				actualProjection = Main.appendProjection(actualProjection, "GROUP_CONCAT(DISTINCT " + C.AS_WORD2 + ".lemma) AS " + LexRelations_Senses_Words_X.MEMBERS2);
				table = C.LexRelations.TABLE + " AS " + C.AS_RELATION + ' ' + //
						"INNER JOIN " + C.Relations.TABLE + " USING (linkid) " + //
						"INNER JOIN "+C.Synsets.TABLE +" AS " + C.AS_DEST + " ON " + C.AS_RELATION + ".synset2id = " + C.AS_DEST + ".synsetid " + //
						"INNER JOIN " + C.Words.TABLE + " AS " + C.AS_WORD + " ON " + C.AS_RELATION + ".word2id = " + C.AS_WORD + ".wordid " + //
						"LEFT JOIN " + C.Senses.TABLE + " AS " + C.AS_SENSE + " ON " + C.AS_DEST + ".synsetid = " + C.AS_SENSE + ".synsetid " + //
						"LEFT JOIN " + C.Words.TABLE + " AS " + C.AS_WORD2 + " USING (wordid)";
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
				table = C.Words.TABLE + " " + //
						"LEFT JOIN morphmaps USING (wordid) " + //
						"LEFT JOIN morphs USING (morphid)";
				break;

			// T E X T S E A R C H

			case Main.LOOKUP_FTS_WORDS:
				table = C.Words.TABLE + "_lemma_fts4";
				break;

			case Main.LOOKUP_FTS_DEFINITIONS:
				table = ""+C.Synsets.TABLE +"_definition_fts4";
				break;

			case Main.LOOKUP_FTS_SAMPLES:
				table = "samples_sample_fts4";
				break;

			// S U G G E S T

			case Main.SUGGEST_WORDS:
			{
				table = C.Words.TABLE + "";
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
				table = C.Words.TABLE + "_lemma_fts4";
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
				table = ""+C.Synsets.TABLE +"_definition_fts4";
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
		return new String[]{
				"T:" + table, //
				"P:" + Arrays.toString(actualProjection), //
				"S:" + actualSelection, //
				"A:" + Arrays.toString(actualSelectionArgs), //
				"G:" + groupBy
		};
	}
}
