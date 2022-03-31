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
public class Q0 implements Function<String,String[]>
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
				table = "(" + //
						"SELECT fnwordid + 10000 AS " + LexUnits_or_Frames.ID + ", luid AS " + LexUnits_or_Frames.FNID + ", fnwordid AS " + LexUnits_or_Frames.FNWORDID + ", wordid AS " + LexUnits_or_Frames.WORDID + ", word AS " + LexUnits_or_Frames.WORD + ", lexunit AS " + LexUnits_or_Frames.NAME + ", frame AS " + LexUnits_or_Frames.FRAMENAME + ", frameid AS " + LexUnits_or_Frames.FRAMEID + ", 0 AS " + LexUnits_or_Frames.ISFRAME + " " + //
						"FROM fnwords " + //
						"INNER JOIN fnlexemes USING (fnwordid) " + //
						"INNER JOIN fnlexunits AS " + C.LU + " USING (luid) " + //
						"INNER JOIN fnframes AS " + C.FRAME + " USING (frameid) " + //
						"UNION " + //
						"SELECT frameid AS " + LexUnits_or_Frames.ID + ", frameid AS " + LexUnits_or_Frames.FNID + ", 0 AS " + LexUnits_or_Frames.FNWORDID + ", 0 AS " + LexUnits_or_Frames.WORDID + ", frame AS " + LexUnits_or_Frames.WORD + ", frame AS " + LexUnits_or_Frames.NAME + ", frame AS " + LexUnits_or_Frames.FRAMENAME + ", frameid AS " + LexUnits_or_Frames.FRAMEID + ", 1 AS " + LexUnits_or_Frames.ISFRAME + " " + //
						"FROM fnframes " + //
						")";
				break;

			case "FRAMES_X_BY_FRAME":
				groupBy = "frameid";
				table = "fnframes " + //
						"LEFT JOIN fnframes_semtypes USING (frameid) " + //
						"LEFT JOIN fnsemtypes USING (semtypeid)";
				break;

			case "FRAMES_RELATED":
				table = "fnframes_related AS " + C.RELATED + ' ' + //
						"LEFT JOIN fnframes AS " + C.SRC + " USING (frameid) " + //
						"LEFT JOIN fnframes AS " + C.DEST + " ON (frame2id = " + C.DEST + ".frameid) " + //  //  //
						"LEFT JOIN fnframerelations USING (relationid)";
				break;

			case "LEXUNITS_X_BY_LEXUNIT":
				groupBy = "luid";
				table = "fnlexunits AS " + C.LU + ' ' + //
						"LEFT JOIN fnframes AS " + C.FRAME + " USING (frameid) " + //
						"LEFT JOIN fnposes AS " + C.POS + " ON (" + C.LU + ".posid = " + C.POS + ".posid) " + //
						"LEFT JOIN fnfetypes AS " + C.FETYPE + " ON (incorporatedfetypeid = " + C.FETYPE + ".fetypeid) " + //
						"LEFT JOIN fnfes AS " + C.FE + " ON (incorporatedfeid = " + C.FE + ".feid)";
				break;

			case "SENTENCES_LAYERS_X":
				table = "(SELECT annosetid,sentenceid,layerid,layertype,rank," + //
						"GROUP_CONCAT(start||':'||" + //
						"end||':'||" + //
						"labeltype||':'||" + //
						"CASE WHEN labelitype IS NULL THEN '' ELSE labelitype END||':'||" + //
						"CASE WHEN bgcolor IS NULL THEN '' ELSE bgcolor END||':'||" + //
						"CASE WHEN fgcolor IS NULL THEN '' ELSE fgcolor END,'|') AS " + Sentences_Layers_X.LAYERANNOTATIONS + ' ' + //
						"FROM fnsentences " + //
						"LEFT JOIN fnannosets USING (sentenceid) " + //
						"LEFT JOIN fnlayers USING (annosetid) " + //
						"LEFT JOIN fnlayertypes USING (layertypeid) " + //
						"LEFT JOIN fnlabels USING (layerid) " + //
						"LEFT JOIN fnlabeltypes USING (labeltypeid) " + //
						"LEFT JOIN fnlabelitypes USING (labelitypeid) " + //
						"WHERE sentenceid = ? AND labeltypeid IS NOT NULL " + //
						"GROUP BY layerid " + //
						"ORDER BY rank,layerid,start,end)";
				break;

			case "ANNOSETS_LAYERS_X":
				table = "(SELECT sentenceid,text,layerid,layertype,rank," + //
						"GROUP_CONCAT(start||':'||" + //
						"end||':'||" + //
						"labeltype||':'||" + //
						"CASE WHEN labelitype IS NULL THEN '' ELSE labelitype END||':'||" + //
						"CASE WHEN bgcolor IS NULL THEN '' ELSE bgcolor END||':'||" + //
						"CASE WHEN fgcolor IS NULL THEN '' ELSE fgcolor END,'|') AS " + AnnoSets_Layers_X.LAYERANNOTATIONS + ' ' + //
						"FROM fnannosets " + //
						"LEFT JOIN fnsentences USING (sentenceid) " + //
						"LEFT JOIN fnlayers USING (annosetid) " + //
						"LEFT JOIN fnlayertypes USING (layertypeid) " + //
						"LEFT JOIN fnlabels USING (layerid) " + //
						"LEFT JOIN fnlabeltypes USING (labeltypeid) " + //
						"LEFT JOIN fnlabelitypes USING (labelitypeid) " + //
						"WHERE annosetid = ? AND labeltypeid IS NOT NULL " + //
						"GROUP BY layerid " + //
						"ORDER BY rank,layerid,start,end)";
				break;

			case "PATTERNS_LAYERS_X":
				table = "(SELECT annosetid,sentenceid,text,layerid,layertype,rank," + //
						"GROUP_CONCAT(start||':'||" + //
						"end||':'||" + //
						"labeltype||':'||" + //
						"CASE WHEN labelitype IS NULL THEN '' ELSE labelitype END||':'||" + //
						"CASE WHEN bgcolor IS NULL THEN '' ELSE bgcolor END||':'||" + //
						"CASE WHEN fgcolor IS NULL THEN '' ELSE fgcolor END,'|') AS " + Patterns_Layers_X.LAYERANNOTATIONS + ' ' + //
						"FROM fnpatterns_annosets " + //
						"LEFT JOIN fnannosets USING (annosetid) " + //
						"LEFT JOIN fnsentences USING (sentenceid) " + //
						"LEFT JOIN fnlayers USING (annosetid) " + //
						"LEFT JOIN fnlayertypes USING (layertypeid) " + //
						"LEFT JOIN fnlabels USING (layerid) " + //
						"LEFT JOIN fnlabeltypes USING (labeltypeid) " + //
						"LEFT JOIN fnlabelitypes USING (labelitypeid) " + //
						"WHERE patternid = ? AND labeltypeid IS NOT NULL " + //
						"GROUP BY layerid " + //
						"ORDER BY rank,layerid,start,end)";
				break;

			case "VALENCEUNITS_LAYERS_X":
				table = "(SELECT annosetid,sentenceid,text,layerid,layertype,rank," + //
						"GROUP_CONCAT(start||':'||" + //
						"end||':'||" + //
						"labeltype||':'||" + //
						"CASE WHEN labelitype IS NULL THEN '' ELSE labelitype END||':'||" + //
						"CASE WHEN bgcolor IS NULL THEN '' ELSE bgcolor END||':'||" + //
						"CASE WHEN fgcolor IS NULL THEN '' ELSE fgcolor END,'|') AS " + ValenceUnits_Layers_X.LAYERANNOTATIONS + ' ' + //
						"FROM fnvalenceunits_annosets " + //
						"LEFT JOIN fnannosets USING (annosetid) " + //
						"LEFT JOIN fnsentences USING (sentenceid) " + //
						"LEFT JOIN fnlayers USING (annosetid) " + //
						"LEFT JOIN fnlayertypes USING (layertypeid) " + //
						"LEFT JOIN fnlabels USING (layerid) " + //
						"LEFT JOIN fnlabeltypes USING (labeltypeid) " + //
						"LEFT JOIN fnlabelitypes USING (labelitypeid) " + //
						"WHERE vuid = ? AND labeltypeid IS NOT NULL " + //
						"GROUP BY layerid " + //
						"ORDER BY rank,layerid,start,end)";
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
