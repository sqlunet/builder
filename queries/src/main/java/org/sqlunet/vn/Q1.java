/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package org.sqlunet.vn;

import org.sqlbuilder.common.Q;
import org.sqlunet.vn.C.VnClasses;

import java.util.Arrays;

/**
 * VerbNet provider
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
				table = String.format("%s " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s)", //
						VnClasses.TABLE, //
						"vnmembers_vngroupings", "classid", //
						"vngroupings", "groupingid"); //
				groupBy = "classid";
				break;

			// J O I N S

			case "WORDS_VNCLASSES":
				table = String.format("%s " + //
								"INNER JOIN %s USING (%s) " + //
								"INNER JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s)", //
						"words", //
						"vnwords", "wordid", //
						"vnmembers_senses", "vnwordid", //
						"vnclasses", "classid");
				break;

			case "VNCLASSES_VNMEMBERS_X_BY_WORD":
				table = String.format("%s " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s, %s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s)", //
						"vnmembers_senses", //
						"vnwords", "vnwordid", //
						"vnmembers_vngroupings", "classid", "vnwordid", //
						"vngroupings", "groupingid", //
						"synsets", "synsetid");
				groupBy = "vnwordid";
				break;

			case "VNCLASSES_VNROLES_X_BY_VNROLE":
				table = String.format("%s " + //
								"INNER JOIN %s USING (%s) " + //
								"INNER JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s)", //
						"vnclasses_vnroles", //
						"vnroles", "roleid", //
						"vnroletypes", "roletypeid", //
						"vnrestrs", "restrsid");
				groupBy = "roleid";
				break;

			case "VNCLASSES_VNFRAMES_X_BY_VNFRAME":
				table = String.format("%s " + //
								"INNER JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s)", //
						"vnclasses_vnframes", //
						"vnframes", "frameid", //
						"vnframenames", "nameid", //
						"vnframesubnames", "subnameid", //
						"vnsyntaxes", "syntaxid", //
						"vnsemantics", "semanticsid", //
						"vnframes_examples", "frameid", //
						"vnexamples", "exampleid");
				groupBy = "frameid";
				break;

			// L O O K U P

			case "LOOKUP_FTS_EXAMPLES":
				table = String.format("%s_%s_fts4", "vnexamples", "example");
				break;

			case "LOOKUP_FTS_EXAMPLES_X_BY_EXAMPLE":
				groupBy = "exampleid";
				//$FALL-THROUGH$
				//noinspection fallthrough
			case "LOOKUP_FTS_EXAMPLES_X":
				table = String.format("%s_%s_fts4 " + //
								"LEFT JOIN %s USING (%s)", //
						"vnexamples", "example", //
						"vnclasses", "classid");
				break;

			// S U G G E S T

			case "SUGGEST_WORDS":
			{
				table = "vnwords";
				actualProjection = new String[]{"vnwordid AS _id", //
						String.format("%s AS " + "SearchManager.SUGGEST_COLUMN_TEXT_1", "lemma"), //
						String.format("%s AS " + "SearchManager.SUGGEST_COLUMN_QUERY", "lemma")}; //
				actualSelection = String.format("%s LIKE ? || '%%'", "lemma");
				actualSelectionArgs = new String[]{last};
				break;
			}

			case "SUGGEST_FTS_WORDS":
			{
				table = String.format("%s_%s_fts4", "vnwords", "lemma");
				actualProjection = new String[]{String.format("%s AS _id", "vnwordid"),//
						String.format("%s AS " + "SearchManager.SUGGEST_COLUMN_TEXT_1", "lemma"), //
						String.format("%s AS " + "SearchManager.SUGGEST_COLUMN_QUERY", "lemma")}; //
				actualSelection = String.format("%s MATCH ?", "lemma");
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