/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package org.sqlunet.vn;

import org.sqlunet.vn.C.VnClasses;

import java.util.Arrays;
import java.util.function.Function;

/**
 * VerbNet provider
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
			// I T E M
			// the incoming URI was for a single item because this URI was for a single row, the _ID value part is present.
			// get the last path segment from the URI: this is the _ID value. then, append the value to the WHERE clause for the query

			case "VNCLASS":
				table = VnClasses.TABLE;
				if (actualSelection != null)
				{
					actualSelection += " AND ";
				}
				else
				{
					actualSelection = "";
				}
				actualSelection += VnClasses.CLASSID + " = " + "URI_PATH_SEGMENT";
				break;

			case "VNCLASSES":
				table = VnClasses.TABLE;
				break;

			case "VNCLASSES_X_BY_VNCLASS":
				groupBy = "classid";
				table = "vnclasses " + //
						"LEFT JOIN vngroupingmaps USING (classid) " + //
						"LEFT JOIN vngroupings USING (groupingid)";
				break;

			// J O I N S

			case "WORDS_VNCLASSES":
				table = "words " + //
						"INNER JOIN vnwords USING (wordid) " + //
						"INNER JOIN vnclassmembersenses USING (vnwordid) " + //
						"LEFT JOIN vnclasses USING (classid)";
				break;

			case "VNCLASSES_VNMEMBERS_X_BY_WORD":
				groupBy = "vnwordid";
				table = "vnclassmembersenses " + //
						"LEFT JOIN vnwords USING (vnwordid) " + //
						"LEFT JOIN vngroupingmaps USING (classid, vnwordid) " + //
						"LEFT JOIN vngroupings USING (groupingid) " + //
						"LEFT JOIN synsets USING (synsetid)";
				break;

			case "VNCLASSES_VNROLES_X_BY_VNROLE":
				groupBy = "roleid";
				table = "vnrolemaps " + //
						"INNER JOIN vnroles USING (roleid) " + //
						"INNER JOIN vnroletypes USING (roletypeid) " + //
						"LEFT JOIN vnrestrs USING (restrsid)";
				break;

			case "VNCLASSES_VNFRAMES_X_BY_VNFRAME":
				groupBy = "frameid";
				table = "vnframemaps " + //
						"INNER JOIN vnframes USING (frameid) " + //
						"LEFT JOIN vnframenames USING (nameid) " + //
						"LEFT JOIN vnframesubnames USING (subnameid) " + //
						"LEFT JOIN vnsyntaxes USING (syntaxid) " + //
						"LEFT JOIN vnsemantics USING (semanticsid) " + //
						"LEFT JOIN vnexamplemaps USING (frameid) " + //
						"LEFT JOIN vnexamples USING (exampleid)";
				break;

			// L O O K U P

			case "LOOKUP_FTS_EXAMPLES":
				table = "vnexamples_example_fts4";
				break;
			case "LOOKUP_FTS_EXAMPLES_X_BY_EXAMPLE":
				groupBy = "exampleid";
				//$FALL-THROUGH$
				//noinspection fallthrough
			case "LOOKUP_FTS_EXAMPLES_X":
				table = "vnexamples_example_fts4 " + //
						"LEFT JOIN vnclasses USING (classid)";
				break;

			// S U G G E S T

			case "SUGGEST_WORDS":
			{
				table = "vnwords";
				actualProjection = new String[]{"vnwordid AS _id", //
						"lemma AS " + "SearchManager.SUGGEST_COLUMN_TEXT_1", //
						"lemma AS " + "SearchManager.SUGGEST_COLUMN_QUERY"}; //
				actualSelection = "lemma LIKE ? || '%'";
				actualSelectionArgs = new String[]{last};
				break;
			}

			case "SUGGEST_FTS_WORDS":
			{
				table = "vnwords_word_fts4";
				actualProjection = new String[]{"vnwordid AS _id", //
						"lemma AS " + "SearchManager.SUGGEST_COLUMN_TEXT_1", //
						"lemma AS " + "SearchManager.SUGGEST_COLUMN_QUERY"}; //
				actualSelection = "lemma MATCH ?";
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