/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package org.sqlunet.pb;

import org.sqlbuilder.common.Q;
import org.sqlunet.pb.C.*;

import java.util.Arrays;

/**
 * PropBank provider
 *
 * @author <a href="mailto:1313ou@gmail.com">Bernard Bou</a>
 */
public class Q2 implements Q
{
	@Override
	public String[] query(String key)
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
						PbRoleSets.TABLE, //
						"pbrolesetmembers", C.MEMBER, PbRoleSets.ROLESETID, //
						PbWords.TABLE, C.WORD, C.MEMBER, PbWords.PBWORDID, C.WORD, PbWords.PBWORDID);
				break;

			case "WORDS_PBROLESETS":
				table = String.format("%s " + //
								"INNER JOIN %s USING (%s) " + //
								"INNER JOIN %s USING (%s)", //
						"words", //
						PbWords.TABLE, PbWords.WORDID, //
						PbRoleSets.TABLE, PbWords.PBWORDID);
				break;

			case "PBROLESETS_PBROLES":
				table = String.format("%s " + //
								"INNER JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s)", //
						PbRoleSets.TABLE, //
						PbRoles.TABLE, PbRoleSets.ROLESETID, //
						PbFuncs.TABLE, PbFuncs.FUNC, //
						PbThetas.TABLE, PbThetas.THETA);
				actualSortOrder = "narg";
				break;

			case "PBROLESETS_PBEXAMPLES_BY_EXAMPLE":
				groupBy = String.format("%s.%s", C.EXAMPLE, PbExamples.EXAMPLEID);
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
						PbRoleSets.TABLE, //
						PbExamples.TABLE, C.EXAMPLE, PbRoleSets.ROLESETID, //
						PbRels.TABLE, C.REL, PbExamples.EXAMPLEID, //
						PbArgs.TABLE, C.ARG, PbExamples.EXAMPLEID, //
						PbFuncs.TABLE, C.FUNC, C.ARG, PbFuncs.FUNC, C.FUNC, PbFuncs.FUNC, //
						PbAspects.TABLE, PbAspects.ASPECT, //
						PbForms.TABLE, PbForms.FORM, //
						PbTenses.TABLE, PbTenses.TENSE, //
						PbVoices.TABLE, PbVoices.VOICE, //
						PbPersons.TABLE, PbPersons.PERSON, //
						PbRoles.TABLE, PbRoleSets.ROLESETID, PbArgs.ARGN, //
						PbThetas.TABLE, PbThetas.THETA);
				actualSortOrder = String.format("%s.%s,%s", C.EXAMPLE, PbExamples.EXAMPLEID, PbArgs.ARGN);
				break;

			// L O O K U P

			case "LOOKUP_FTS_EXAMPLES":
				table = String.format("%s_%s_fts4", PbExamples.TABLE,  PbExamples.EXAMPLE);
				break;

			case "LOOKUP_FTS_EXAMPLES_X_BY_EXAMPLE":
				groupBy = PbExamples.EXAMPLEID;
				//$FALL-THROUGH$
				//noinspection fallthrough

			case "LOOKUP_FTS_EXAMPLES_X":
				table = String.format("%s_%s_fts4 " + //
								"LEFT JOIN %s USING (%s)", //
						PbExamples.TABLE, "text", //
						PbRoleSets.TABLE, PbRoleSets.ROLESETID);
				break;

			// S U G G E S T

			case "SUGGEST_WORDS":
			{
				table = PbWords.TABLE;
				actualProjection = new String[]{String.format("%s AS _id", PbWords.PBWORDID), //
						String.format("%s AS " + "SearchManager.SUGGEST_COLUMN_TEXT_1", PbWords.WORD), //
						String.format("%s AS " + "SearchManager.SUGGEST_COLUMN_QUERY", PbWords.WORD)}; //
				actualSelection = String.format("%s LIKE ? || '%%'", PbWords.WORD);
				actualSelectionArgs = new String[]{last};
				break;
			}

			case "SUGGEST_FTS_WORDS":
			{
				table = String.format("%s_%s_fts4", PbWords.TABLE, PbWords.WORD);
				actualProjection = new String[]{String.format("%s AS _id", PbWords.PBWORDID), //
						String.format("%s AS " + "SearchManager.SUGGEST_COLUMN_TEXT_1", PbWords.WORD), //
						String.format("%s AS " + "SearchManager.SUGGEST_COLUMN_QUERY", PbWords.WORD)}; //
				actualSelection = String.format("%s MATCH ?", PbWords.WORD); //
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
