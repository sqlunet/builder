/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package org.sqlunet.fn;

import org.sqlbuilder.common.Q;
import org.sqlunet.fn.C.*;

import java.util.Arrays;

/**
 * FrameNet provider
 *
 * @author <a href="mailto:1313ou@gmail.com">Bernard Bou</a>
 */
public class Q1 implements Q
{
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

			case "LEXUNITS":
				table = LexUnits.TABLE;
				break;

			case "FRAMES":
				table = Frames.TABLE;
				break;

			case "ANNOSETS":
				table = AnnoSets.TABLE;
				break;

			case "SENTENCES":
				table = Sentences.TABLE;
				break;

			// I T E M
			// the incoming URI was for a single item because this URI was for a single row, the _ID value part is present.
			// get the last path segment from the URI: this is the _ID value. then, append the value to the WHERE clause for the query

			case "LEXUNIT":
				table = LexUnits.TABLE;
				break;

			case "FRAME":
				table = Frames.TABLE;
				break;

			case "SENTENCE":
				table = Sentences.TABLE;
				break;

			case "ANNOSET":
				table = AnnoSets.TABLE;
				break;

			// J O I N S

			case "LEXUNITS_OR_FRAMES":
				table = String.format("(" + // 1
								"SELECT %s + %s AS %s, %s AS %s, %s AS %s, %s AS %s, %s AS %s, %s AS %s, %s AS %s, %s AS %s, 0 AS %s " + // 2
								"FROM %s " + // 3
								"INNER JOIN %s USING (%s) " + // 4
								"INNER JOIN %s AS %s USING (%s) " + // 5
								"INNER JOIN %s AS %s USING (%s) " + // 6
								"UNION " + // 7
								"SELECT %s AS %s, %s AS %s, 0 AS %s, 0 AS %s, %s AS %s, %s AS %s, %s AS %s, %s AS %s, 1 AS %s " + // 8
								"FROM %s " + // 9
								")", //
						"fnwordid", "10000", LexUnits_or_Frames.ID, "luid", LexUnits_or_Frames.FNID, "fnwordid", LexUnits_or_Frames.FNWORDID, "wordid", LexUnits_or_Frames.WORDID, "word", LexUnits_or_Frames.WORD, "lexunit", LexUnits_or_Frames.NAME, "frame", LexUnits_or_Frames.FRAMENAME, "frameid", LexUnits_or_Frames.FRAMEID, LexUnits_or_Frames.ISFRAME, // 2
						"fnwords", // 3
						"fnlexemes", "fnwordid", // 4
						"fnlexunits", C.LU, "luid", // 5
						"fnframes", C.FRAME, "frameid", // 6
						// 7
						"frameid", LexUnits_or_Frames.ID, "frameid", LexUnits_or_Frames.FNID, LexUnits_or_Frames.FNWORDID, LexUnits_or_Frames.WORDID, "frame", LexUnits_or_Frames.WORD, "frame", LexUnits_or_Frames.NAME, "frame", LexUnits_or_Frames.FRAMENAME, "frameid", LexUnits_or_Frames.FRAMEID, LexUnits_or_Frames.ISFRAME, // 8
						"fnframes"); // 9
				break;

			case "FRAMES_X_BY_FRAME":
				table = String.format("%s " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s)", "fnframes", //
						"fnframes_semtypes", //
						"frameid", //
						"fnsemtypes", "semtypeid");
				groupBy = "frameid";
				break;

			case "FRAMES_RELATED":
				table = String.format("%s AS %s " + //
								"LEFT JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s ON (%s = %s.%s) " + //  //  //
								"LEFT JOIN %s USING (%s)", //
						"fnframes_related", C.RELATED, //
						"fnframes", C.SRC, "frameid", //
						"fnframes", C.DEST, "frame2id", C.DEST, "frameid", //
						"fnframerelations", "relationid");
				break;

			case "LEXUNITS_X_BY_LEXUNIT":
				groupBy = "luid";
				table = String.format("%s AS %s " + //
								"LEFT JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s ON (%s.%s = %s.%s) " + //
								"LEFT JOIN %s AS %s ON (%s = %s.%s) " + //
								"LEFT JOIN %s AS %s ON (%s = %s.%s)", //
						"fnlexunits", C.LU, //
						"fnframes", C.FRAME, "frameid", //
						"fnposes", C.POS, C.LU, "posid", C.POS, "posid", //
						"fnfetypes", C.FETYPE, "incorporatedfetypeid", C.FETYPE, "fetypeid", //
						"fnfes", C.FE, "incorporatedfeid", C.FE, "feid");
				break;

			case "SENTENCES_LAYERS_X":
				table = String.format("(SELECT %s,%s,%s,%s,%s," + // 1
								"GROUP_CONCAT(%s||':'||" + // 2
								"%s||':'||" + // 3
								"%s||':'||" + // 4
								"CASE WHEN %s IS NULL THEN '' ELSE %s END||':'||" + // 5
								"CASE WHEN %s IS NULL THEN '' ELSE %s END||':'||" + // 6
								"CASE WHEN %s IS NULL THEN '' ELSE %s END,'|') AS %s " + // 7
								"FROM %s " + // 8
								"LEFT JOIN %s USING (%s) " + // 9
								"LEFT JOIN %s USING (%s) " + // 10
								"LEFT JOIN %s USING (%s) " + // 11
								"LEFT JOIN %s USING (%s) " + // 12
								"LEFT JOIN %s USING (%s) " + // 13
								"LEFT JOIN %s USING (%s) " + // 14
								"WHERE %s = ? AND %s IS NOT NULL " + // 15
								"GROUP BY %s " + // 16
								"ORDER BY %s,%s,%s,%s)", // 17
						"annosetid", "sentenceid", "layerid", "layertype", "rank", // 1
						"start", // 2
						"end", // 3
						"labeltype", // 4
						"labelitype", "labelitype", // 5
						"bgcolor", "bgcolor", // 6
						"fgcolor", "fgcolor", Sentences_Layers_X.LAYERANNOTATIONS, // 7
						"fnsentences", // 8
						"fnannosets", "sentenceid", // 9
						"fnlayers", "annosetid", // 10
						"fnlayertypes", "layertypeid", // 11
						"fnlabels", "layerid", // 12
						"fnlabeltypes", "labeltypeid", // 13
						"fnlabelitypes", "labelitypeid", // 14
						"sentenceid", "labeltypeid", // 15
						"layerid", // 16
						"rank", "layerid", "start", "end"); // 17
				break;

			case "ANNOSETS_LAYERS_X":
				table = String.format("(SELECT %s,%s,%s,%s,%s," + //
								"GROUP_CONCAT(%s||':'||" + //
								"%s||':'||" + //
								"%s||':'||" + //
								"CASE WHEN %s IS NULL THEN '' ELSE %s END||':'||" + //
								"CASE WHEN %s IS NULL THEN '' ELSE %s END||':'||" + //
								"CASE WHEN %s IS NULL THEN '' ELSE %s END,'|') AS %s " + //
								"FROM %s " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"WHERE %s = ? AND %s IS NOT NULL " + //
								"GROUP BY %s " + //
								"ORDER BY %s,%s,%s,%s)", //
						"sentenceid", "text", "layerid", "layertype", "rank", //
						"start", //
						"end", //
						"labeltype", //
						"labelitype", "labelitype", //
						"bgcolor", "bgcolor", //
						"fgcolor", "fgcolor", AnnoSets_Layers_X.LAYERANNOTATIONS, //
						"fnannosets", //
						"fnsentences", "sentenceid", //
						"fnlayers", "annosetid", //
						"fnlayertypes", "layertypeid", //
						"fnlabels", "layerid", //
						"fnlabeltypes", "labeltypeid", //
						"fnlabelitypes", "labelitypeid", //
						"annosetid", "labeltypeid", //
						"layerid", //
						"rank", "layerid", "start", "end");
				break;

			case "PATTERNS_LAYERS_X":
				table = String.format("(SELECT %s,%s,%s,%s,%s,%s," + //
								"GROUP_CONCAT(%s||':'||" + //
								"%s||':'||" + //
								"%s||':'||" + //
								"CASE WHEN %s IS NULL THEN '' ELSE %s END||':'||" + //
								"CASE WHEN %s IS NULL THEN '' ELSE %s END||':'||" + //
								"CASE WHEN %s IS NULL THEN '' ELSE %s END,'|') AS %s " +//
								"FROM %s " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"WHERE %s = ? AND %s IS NOT NULL " + //
								"GROUP BY %s " + //
								"ORDER BY %s,%s,%s,%s)", //
						"annosetid", "sentenceid", "text", "layerid", "layertype", "rank", //
						"start", //
						"end", //
						"labeltype", //
						"labelitype", "labelitype", //
						"bgcolor", "bgcolor", //
						"fgcolor", "fgcolor", Patterns_Layers_X.LAYERANNOTATIONS, //
						"fnpatterns_annosets", //
						"fnannosets", "annosetid", //
						"fnsentences", "sentenceid", //
						"fnlayers", "annosetid", //
						"fnlayertypes", "layertypeid", //
						"fnlabels", "layerid", //
						"fnlabeltypes", "labeltypeid", //
						"fnlabelitypes", "labelitypeid", //
						"patternid", "labeltypeid", //
						"layerid", //
						"rank", "layerid", "start", "end");
				break;

			case "VALENCEUNITS_LAYERS_X":
				table = String.format("(SELECT %s,%s,%s,%s,%s,%s," + //
								"GROUP_CONCAT(%s||':'||" + //
								"%s||':'||" + //
								"%s||':'||" + //
								"CASE WHEN %s IS NULL THEN '' ELSE %s END||':'||" + //
								"CASE WHEN %s IS NULL THEN '' ELSE %s END||':'||" + //
								"CASE WHEN %s IS NULL THEN '' ELSE %s END,'|') AS %s " + //
								"FROM %s " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"WHERE %s = ? AND %s IS NOT NULL " + //
								"GROUP BY %s " + //
								"ORDER BY %s,%s,%s,%s)", //
						"annosetid", "sentenceid", "text", "layerid", "layertype", "rank", //
						"start", //
						"end", //
						"labeltype", //
						"labelitype", "labelitype", //
						"bgcolor", "bgcolor", //
						"fgcolor", "fgcolor", ValenceUnits_Layers_X.LAYERANNOTATIONS, //
						"fnvalenceunits_annosets", //
						"fnannosets", "annosetid", //
						"fnsentences", "sentenceid", //
						"fnlayers", "annosetid", //
						"fnlayertypes", "layertypeid", //
						"fnlabels", "layerid", //
						"fnlabeltypes", "labeltypeid", //
						"fnlabelitypes", "labelitypeid", //
						"vuid", "labeltypeid", //
						"layerid", //
						"rank", "layerid", "start", "end");
				break;

			case "WORDS_LEXUNITS_FRAMES":
				table = "words " + //
						"INNER JOIN fnwords USING (wordid) " + //
						"INNER JOIN fnlexemes USING (fnwordid) " + //
						"INNER JOIN fnlexunits AS " + C.LU + " USING (luid) " + //
						"LEFT JOIN fnframes USING (frameid) " + //
						"LEFT JOIN fnposes AS " + C.POS + " ON (" + C.LU + ".posid = " + C.POS + ".posid) " + //
						"LEFT JOIN fnfes AS " + C.FE + " ON (incorporatedfeid = feid) " + //
						"LEFT JOIN fnfetypes AS " + C.FETYPE + " ON (incorporatedfetypeid = " + C.FE + ".fetypeid)";
				break;

			case "FRAMES_FES_BY_FE":
				groupBy = "feid";
				//$FALL-THROUGH$
				//noinspection fallthrough
			case "FRAMES_FES":
				table = "fnframes " + //
						"INNER JOIN fnfes USING (frameid) " + //
						"LEFT JOIN fnfetypes USING (fetypeid) " + //
						"LEFT JOIN fncoretypes USING (coretypeid) " + //
						"LEFT JOIN fnfes_semtypes USING (feid) " + //
						"LEFT JOIN fnsemtypes USING (semtypeid)";
				break;

			case "LEXUNITS_SENTENCES_BY_SENTENCE":
				groupBy = C.SENTENCE + ".sentenceid";
				//$FALL-THROUGH$
				//noinspection fallthrough
			case "LEXUNITS_SENTENCES":
				table = "fnlexunits AS " + C.LU + ' ' + //
						"LEFT JOIN fnsubcorpuses USING (luid) " + //
						"LEFT JOIN fnsubcorpuses_sentences USING (subcorpusid) " + //
						"INNER JOIN fnsentences AS " + C.SENTENCE + " USING (sentenceid)";
				break;

			case "LEXUNITS_SENTENCES_ANNOSETS_LAYERS_LABELS_BY_SENTENCE":
				groupBy = C.SENTENCE + ".sentenceid";
				//$FALL-THROUGH$
				//noinspection fallthrough
			case "LEXUNITS_SENTENCES_ANNOSETS_LAYERS_LABELS":
				table = "fnlexunits AS " + C.LU + ' ' + //
						"LEFT JOIN fnsubcorpuses USING (luid) " + //
						"LEFT JOIN fnsubcorpuses_sentences USING (subcorpusid) " + //
						"INNER JOIN fnsentences AS " + C.SENTENCE + " USING (sentenceid) " + //
						"LEFT JOIN fnannosets USING (sentenceid) " + //
						"LEFT JOIN fnlayers USING (annosetid) " + //
						"LEFT JOIN fnlayertypes USING (layertypeid) " + //
						"LEFT JOIN fnlabels USING (layerid) " + //
						"LEFT JOIN fnlabeltypes USING (labeltypeid) " + //
						"LEFT JOIN fnlabelitypes USING (labelitypeid)";
				break;

			case "LEXUNITS_GOVERNORS":
				table = "fnlexunits " + //
						"INNER JOIN fnlexunits_governors USING (luid) " + //
						"INNER JOIN fngovernors USING (governorid) " + //
						"LEFT JOIN fnwords USING (fnwordid)";
				break;

			case "GOVERNORS_ANNOSETS":
				table = "fngovernors_annosets " + //
						"LEFT JOIN fnannosets USING (annosetid) " + //
						"LEFT JOIN fnsentences USING (sentenceid)";
				break;

			case "LEXUNITS_REALIZATIONS_BY_REALIZATION":
				groupBy = "ferid";
				//$FALL-THROUGH$
				//noinspection fallthrough
			case "LEXUNITS_REALIZATIONS":
				table = "fnlexunits " + //
						"INNER JOIN fnferealizations USING (luid) " + //
						"LEFT JOIN fnvalenceunits USING (ferid) " + //
						"LEFT JOIN fnfetypes USING (fetypeid) " + //
						"LEFT JOIN fngftypes USING (gfid) " + //
						"LEFT JOIN fnpttypes USING (ptid)";
				break;

			case "LEXUNITS_GROUPREALIZATIONS_BY_PATTERN":
				groupBy = "patternid";
				//$FALL-THROUGH$
				//noinspection fallthrough
			case "LEXUNITS_GROUPREALIZATIONS":
				table = "fnlexunits " + //
						"INNER JOIN fnfegrouprealizations USING (luid) " + //
						"LEFT JOIN fnpatterns USING (fegrid) " + //
						"LEFT JOIN fnpatterns_valenceunits USING (patternid) " + //
						"LEFT JOIN fnvalenceunits USING (vuid) " + //
						"LEFT JOIN fnfetypes USING (fetypeid) " + //
						"LEFT JOIN fngftypes USING (gfid) " + //
						"LEFT JOIN fnpttypes USING (ptid)";
				break;

			case "PATTERNS_SENTENCES":
				table = "fnpatterns_annosets " + //
						"LEFT JOIN fnannosets AS " + C.ANNOSET + " USING (annosetid) " + //
						"LEFT JOIN fnsentences AS " + C.SENTENCE + " USING (sentenceid)";
				break;

			case "VALENCEUNITS_SENTENCES":
				table = "fnvalenceunits_annosets " + //
						"LEFT JOIN fnannosets AS " + C.ANNOSET + " USING (annosetid) " + //
						"LEFT JOIN fnsentences AS " + C.SENTENCE + " USING (sentenceid)";
				break;

			// L O O K U P

			case "LOOKUP_FTS_WORDS":
				table = "fnwords_word_fts4";
				break;
			case "LOOKUP_FTS_SENTENCES":
				table = "fnsentences_text_fts4";
				break;
			case "LOOKUP_FTS_SENTENCES_X_BY_SENTENCE":
				groupBy = "sentenceid";
				//addProjection(projection, "GROUP_CONCAT(DISTINCT  frame || '@' || frameid)", "GROUP_CONCAT(DISTINCT  lexunit || '@' || luid)");
				//$FALL-THROUGH$
				//noinspection fallthrough
			case "LOOKUP_FTS_SENTENCES_X":
				table = "fnsentences_text_fts4 " + //
						"LEFT JOIN fnframes USING (frameid) " + //
						"LEFT JOIN fnlexunits USING (frameid,luid)";
				break;

			// S U G G E S T

			case "SUGGEST_WORDS":
			{
				table = "fnwords";
				actualProjection = new String[]{"fnwordid AS _id", //
						"word AS " + "SearchManager.SUGGEST_COLUMN_TEXT_1", //
						"word AS " + "SearchManager.SUGGEST_COLUMN_QUERY"};
				actualSelection = "word LIKE ? || '%'";
				actualSelectionArgs = new String[]{last};
				break;
			}

			case "SUGGEST_FTS_WORDS":
			{
				table = "fnwords_word_fts4";
				actualProjection = new String[]{"fnwordid AS _id", //
						"word AS " + "SearchManager.SUGGEST_COLUMN_TEXT_1", //
						"word AS " + "SearchManager.SUGGEST_COLUMN_QUERY"};
				actualSelection = "word MATCH ?";
				actualSelectionArgs = new String[]{last + '*'};
				break;
			}

			default:
				return null;
		}
		return new String[]{table, //
				Arrays.toString(actualProjection), //
				actualSelection, //
				Arrays.toString(actualSelectionArgs), //
				groupBy};
	}
}
