/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package org.sqlunet.vn;

import org.sqlbuilder.common.Q;
import org.sqlunet.vn.C.*;

import java.util.Arrays;

/**
 * VerbNet provider
 *
 * @author <a href="mailto:1313ou@gmail.com">Bernard Bou</a>
 */
public class QC implements Q
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
						VnMembers_VnGroupings.TABLE, VnClasses.CLASSID, //
						VnGroupings.TABLE, VnGroupings.GROUPINGID); //
				groupBy = VnClasses.CLASSID;
				break;

			// J O I N S

			case "WORDS_VNCLASSES":
				table = String.format("%s " + //
								"INNER JOIN %s USING (%s) " + //
								"INNER JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s)", //
						Words.TABLE, //
						VnWords.TABLE, Words.WORDID, //
						VnMembers_Senses.TABLE, VnWords.VNWORDID, //
						VnClasses.TABLE, VnClasses.CLASSID);
				break;

			case "VNCLASSES_VNMEMBERS_X_BY_WORD":
				table = String.format("%s " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s, %s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s)", //
						VnMembers_Senses.TABLE, //
						VnWords.TABLE, VnWords.VNWORDID, //
						VnMembers_VnGroupings.TABLE, VnMembers.CLASSID, VnMembers.VNWORDID, //
						VnGroupings.TABLE, VnGroupings.GROUPINGID, //
						Synsets.TABLE, Synsets.SYNSETID);
				groupBy = VnWords.VNWORDID;
				break;

			case "VNCLASSES_VNROLES_X_BY_VNROLE":
				table = String.format("%s " + //
								"INNER JOIN %s USING (%s) " + //
								"INNER JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s)", //
						VnClasses_VnRoles.TABLE, //
						VnRoles.TABLE, VnRoles.ROLEID, //
						VnRoleTypes.TABLE, VnRoleTypes.ROLETYPEID, //
						VnRestrs.TABLE, VnRestrs.RESTRSID);
				groupBy = VnRoles.ROLEID;
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
						VnClasses_VnFrames.TABLE, //
						VnFrames.TABLE, VnFrames.FRAMEID, //
						VnFrameNames.TABLE, VnFrameNames.FRAMENAMEID, //
						VnFrameSubNames.TABLE, VnFrameSubNames.FRAMESUBNAMEID, //
						VnSyntaxes.TABLE, VnSyntaxes.SYNTAXID, //
						VnSemantics.TABLE, VnSemantics.SEMANTICSID, //
						VnFrames_VnExamples.TABLE, VnFrames.FRAMEID, //
						VnExamples.TABLE, VnExamples.EXAMPLEID);
				groupBy = VnFrames.FRAMEID;
				break;

			// L O O K U P

			case "LOOKUP_FTS_EXAMPLES":
				table = String.format("%s_%s_fts4", VnExamples.TABLE, VnExamples.EXAMPLE);
				break;

			case "LOOKUP_FTS_EXAMPLES_X_BY_EXAMPLE":
				groupBy = VnExamples.EXAMPLEID;
				//$FALL-THROUGH$
				//noinspection fallthrough
			case "LOOKUP_FTS_EXAMPLES_X":
				table = String.format("%s_%s_fts4 " + //
								"LEFT JOIN %s USING (%s)", //
						VnExamples.TABLE, VnExamples.EXAMPLE, //
						VnClasses.TABLE, VnClasses.CLASSID);
				break;

			// S U G G E S T

			case "SUGGEST_WORDS":
			{
				table = VnWords.TABLE;
				actualProjection = new String[]{String.format("%s AS _id", VnWords.VNWORDID), //
						String.format("%s AS " + "SearchManager.SUGGEST_COLUMN_TEXT_1", VnWords.WORD), //
						String.format("%s AS " + "SearchManager.SUGGEST_COLUMN_QUERY", VnWords.WORD)}; //
				actualSelection = String.format("%s LIKE ? || '%%'", VnWords.WORD);
				actualSelectionArgs = new String[]{last};
				break;
			}

			case "SUGGEST_FTS_WORDS":
			{
				table = String.format("%s_%s_fts4", VnWords.TABLE, VnWords.WORD);
				actualProjection = new String[]{String.format("%s AS _id", VnWords.VNWORDID),//
						String.format("%s AS " + "SearchManager.SUGGEST_COLUMN_TEXT_1", VnWords.WORD), //
						String.format("%s AS " + "SearchManager.SUGGEST_COLUMN_QUERY", VnWords.WORD)}; //
				actualSelection = String.format("%s MATCH ?", VnWords.WORD);
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