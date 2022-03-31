/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package org.sqlunet.pb;

import org.sqlunet.pb.C.PbRoleSets;
import org.sqlunet.pb.C.PbRoleSets_X;

import java.util.Arrays;
import java.util.function.Function;

/**
 * PropBank provider
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
		final String sortOrder = "SORT";

		String[] actualProjection = projection;
		String actualSelection = selection;
		String[] actualSelectionArgs = selectionArgs;
		String actualSortOrder = sortOrder;
		String groupBy = null;
		String table;

		switch (key)
		{
			// I T E M
			// the incoming URI was for a single item because this URI was for a single row, the _ID value part is present.
			// get the last path segment from the URI: this is the _ID value. then, append the value to the WHERE clause for the query

			case "PBROLESET":
				table = PbRoleSets.TABLE;
				if (actualSelection != null)
				{
					actualSelection += " AND ";
				}
				else
				{
					actualSelection = "";
				}
				actualSelection += String.format("%s = ?", PbRoleSets.ROLESETID);
				break;

			case "PBROLESETS":
				table = PbRoleSets.TABLE;
				break;

			// J O I N S

			case "PBROLESETS_X_BY_ROLESET":
				groupBy = PbRoleSets_X.ROLESETID;
				//$FALL-THROUGH$
				//noinspection fallthrough
			case "PBROLESETS_X":
				table = String.format("%s " + //
								"LEFT JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s ON %s.%s = %s.%s", //
						"pbrolesets", //
						"pbrolesetmembers", C.MEMBER, "rolesetid", //
						"pbwords", C.WORD, C.MEMBER, "pbwordid", C.WORD, "pbwordid");
				break;

			case "WORDS_PBROLESETS":
				table = String.format("%s " + //
								"INNER JOIN %s USING (%s) " + //
								"INNER JOIN %s USING (%s)", "words", //
						"pbwords", "wordid", //
						"pbrolesets", "pbwordid");
				break;

			case "PBROLESETS_PBROLES":
				table = String.format("%s " + //
								"INNER JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s)", //
						"pbrolesets", //
						"pbroles", "rolesetid", //
						"pbfuncs", "func", //
						"pbthetas", "theta");
				actualSortOrder = "narg";
				break;

			case "PBROLESETS_PBEXAMPLES_BY_EXAMPLE":
				groupBy = C.EXAMPLE + ".exampleid";
				//$FALL-THROUGH$
				//noinspection fallthrough
			case "PBROLESETS_PBEXAMPLES":
				table = String.format("%s " + //
								"INNER JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s USING (%s) " + //
								"LEFT JOIN %s AS %s ON (%s.%s = %s.%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s,%s) " + //
								"LEFT JOIN %s USING (%s)", //
						"pbrolesets", //
						"pbexamples", C.EXAMPLE, "rolesetid", //
						"pbrels", C.REL, "exampleid", //
						"pbargs", C.ARG, "exampleid", //
						"pbfuncs", C.FUNC, C.ARG, "func", C.FUNC, "func", //
						"pbaspects", "aspect", //
						"pbforms", "form", //
						"pbtenses", "tense", //
						"pbvoices", "voice", //
						"pbpersons", "person", //
						"pbroles", "rolesetid", "narg", //
						"pbthetas", "theta");
				actualSortOrder = String.format("%s.%s,%s", C.EXAMPLE, "exampleid", "narg");
				break;

			// L O O K U P

			case "LOOKUP_FTS_EXAMPLES":
				table = String.format("%s_%s_fts4", "pbexamples", "text");
				break;

			case "LOOKUP_FTS_EXAMPLES_X_BY_EXAMPLE":
				groupBy = "exampleid";
				//$FALL-THROUGH$
				//noinspection fallthrough

			case "LOOKUP_FTS_EXAMPLES_X":
				table = String.format("%s_%s_fts4 " + //
								"LEFT JOIN pbrolesets USING (rolesetid)", //
						"pbexamples", "text", //
						"pbrolesets", "rolesetid");
				break;

			// S U G G E S T

			case "SUGGEST_WORDS":
			{
				table = "pbwords";
				actualProjection = new String[]{String.format("%s AS _id", "pbwordid"), //
						String.format("%s AS " + "SearchManager.SUGGEST_COLUMN_TEXT_1", "word"), //
						String.format("%s AS " + "SearchManager.SUGGEST_COLUMN_QUERY", "word")}; //
				actualSelection = String.format("%s LIKE ? || '%%'", "word");
				actualSelectionArgs = new String[]{last};
				break;
			}

			case "SUGGEST_FTS_WORDS":
			{
				table = String.format("%s_%s_fts4", "pbwords", "word");
				actualProjection = new String[]{String.format("%s AS _id", "pbwordid"), //
						String.format("%s AS " + "SearchManager.SUGGEST_COLUMN_TEXT_1", "word"), //
						String.format("%s AS " + "SearchManager.SUGGEST_COLUMN_QUERY", "word")}; //
				actualSelection = String.format("%s MATCH ?", "word"); //
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
