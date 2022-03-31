/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package org.sqlunet.fn;

import org.sqlunet.fn.C.*;

import java.util.Arrays;
import java.util.function.Function;

/**
 * FrameNet provider
 *
 * @author <a href="mailto:1313ou@gmail.com">Bernard Bou</a>
 */
public class Q1 implements Function<String,String[]>
{
	@Override
	public String[] apply(String key)
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
				table = String.format("%s " + //
								"INNER JOIN %s USING (%s) " + //
								"INNER JOIN %s USING (%s) " + //
								"INNER JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s AS %s ON (%s.%s = %s.%s) " + //
								"LEFT JOIN %s AS %s ON (%s = %s) " + //
								"LEFT JOIN %s AS %s ON (%s = %s.%s)", "words", //
						"fnwords", "wordid", //
						"fnlexemes", "fnwordid", //
						"fnlexunits", C.LU, "luid", //
						"fnframes", "frameid", //
						"fnposes", C.POS, C.LU, "posid", C.POS, "posid", //
						"fnfes", C.FE, "incorporatedfeid", "feid", //
						"fnfetypes", C.FETYPE, "incorporatedfetypeid", C.FE, "fetypeid");
				break;

			case "FRAMES_FES_BY_FE":
				groupBy = "feid";
				//$FALL-THROUGH$
				//noinspection fallthrough
			case "FRAMES_FES":
				table = String.format("%s " + //
								"INNER JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s)", "fnframes", //
						"fnfes", "frameid", //
						"fnfetypes", "fetypeid", //
						"fncoretypes", "coretypeid", //
						"fnfes_semtypes", "feid", //
						"fnsemtypes", "semtypeid");
				break;

			case "LEXUNITS_SENTENCES_BY_SENTENCE":
				groupBy = C.SENTENCE + ".sentenceid";
				//$FALL-THROUGH$
				//noinspection fallthrough
			case "LEXUNITS_SENTENCES":
				table = String.format("%s AS %s " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"INNER JOIN %s AS %s USING (%s)", //
						"fnlexunits", C.LU, //
						"fnsubcorpuses", "luid", //
						"fnsubcorpuses_sentences", "subcorpusid", //
						"fnsentences", C.SENTENCE, "sentenceid");
				break;

			case "LEXUNITS_SENTENCES_ANNOSETS_LAYERS_LABELS_BY_SENTENCE":
				groupBy = C.SENTENCE + ".sentenceid";
				//$FALL-THROUGH$
				//noinspection fallthrough
			case "LEXUNITS_SENTENCES_ANNOSETS_LAYERS_LABELS":
				table = String.format("%s AS %s " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"INNER JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s)", "fnlexunits", C.LU, //
						"fnsubcorpuses", "luid", //
						"fnsubcorpuses_sentences", "subcorpusid", //
						"fnsentences", C.SENTENCE, "sentenceid", //
						"fnannosets", "sentenceid", //
						"fnlayers", "annosetid", //
						"fnlayertypes", "layertypeid", //
						"fnlabels", "layerid", //
						"fnlabeltypes", "labeltypeid", //
						"fnlabelitypes", "labelitypeid");
				break;

			case "LEXUNITS_GOVERNORS":
				table = String.format("%s " + //
								"INNER JOIN %s USING (%s) " + //
								"INNER JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s)", //
						"fnlexunits", //
						"fnlexunits_governors", "luid", //
						"fngovernors", "governorid", //
						"fnwords", "fnwordid");
				break;

			case "GOVERNORS_ANNOSETS":
				table = String.format("%s " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s)", //
						"fngovernors_annosets", //
						"fnannosets", "annosetid", //
						"fnsentences", "sentenceid");
				break;

			case "LEXUNITS_REALIZATIONS_BY_REALIZATION":
				groupBy = "ferid";
				//$FALL-THROUGH$
				//noinspection fallthrough
			case "LEXUNITS_REALIZATIONS":
				table = String.format("%s " + //
								"INNER JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s)", //
						"fnlexunits", //
						"fnferealizations", "luid", //
						"fnvalenceunits", "ferid", //
						"fnfetypes", "fetypeid", //
						"fngftypes", "gfid", //
						"fnpttypes", "ptid");
				break;

			case "LEXUNITS_GROUPREALIZATIONS_BY_PATTERN":
				groupBy = "patternid";
				//$FALL-THROUGH$
				//noinspection fallthrough
			case "LEXUNITS_GROUPREALIZATIONS":
				table = String.format("%s " + //
								"INNER JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s)", //
						"fnlexunits", //
						"fnfegrouprealizations", "luid", //
						"fnpatterns", "fegrid", //
						"fnpatterns_valenceunits", "patternid", //
						"fnvalenceunits", "vuid", //
						"fnfetypes", "fetypeid", //
						"fngftypes", "gfid", //
						"fnpttypes", "ptid");
				break;

			case "PATTERNS_SENTENCES":
				table = String.format("%s " + //
								"LEFT JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s USING (%s)", //
						"fnpatterns_annosets", //
						"fnannosets", C.ANNOSET, "annosetid", //
						"fnsentences", C.SENTENCE, "sentenceid");
				break;

			case "VALENCEUNITS_SENTENCES":
				table = String.format("%s " + //
								"LEFT JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s USING (%s)", //
						"fnvalenceunits_annosets", //
						"fnannosets", C.ANNOSET, "annosetid", //
						"fnsentences", C.SENTENCE, "sentenceid");
				break;

			// L O O K U P

			case "LOOKUP_FTS_WORDS":
				table = String.format("%s_%s_fts4", "fnwords", "word");
				break;

			case "LOOKUP_FTS_SENTENCES":
				table = String.format("%s_%s_fts4", "fnsentences", "text");
				break;

			case "LOOKUP_FTS_SENTENCES_X_BY_SENTENCE":
				groupBy = "sentenceid";
				//addProjection(projection, "GROUP_CONCAT(DISTINCT  frame || '@' || frameid)", "GROUP_CONCAT(DISTINCT  lexunit || '@' || luid)");
				//$FALL-THROUGH$
				//noinspection fallthrough

			case "LOOKUP_FTS_SENTENCES_X":
				table = String.format("%s_%s_fts4 " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s,%s)", //
						"fnsentences", "text", //
						"fnframes", "frameid", //
						"fnlexunits", "frameid", "luid");
				break;

			// S U G G E S T

			case "SUGGEST_WORDS":
			{
				table = "fnwords";
				actualProjection = new String[]{String.format("%s AS _id", "fnwordid"), //
						String.format("%s AS %s", "word", "SearchManager.SUGGEST_COLUMN_TEXT_1"), //
						String.format("%s AS %s", "word", "SearchManager.SUGGEST_COLUMN_QUERY")};
				actualSelection = String.format("%s LIKE ? || '%%'", "word");
				actualSelectionArgs = new String[]{String.format("%s", last)};
				break;
			}

			case "SUGGEST_FTS_WORDS":
			{
				table = String.format("%s_%s_fts4", "fnwords", "word");
				actualProjection = new String[]{String.format("%s AS _id", "fnwordid"), //
						String.format("%s AS %s", "word", "SearchManager.SUGGEST_COLUMN_TEXT_1"), //
						String.format("%s AS %s", "word", "SearchManager.SUGGEST_COLUMN_QUERY")};
				actualSelection = String.format("%s MATCH ?", "word");
				actualSelectionArgs = new String[]{String.format("%s*", last)};
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
