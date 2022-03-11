/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package org.sqlunet.pb;

import org.sqlbuilder.common.Q;
import org.sqlunet.pb.C.PbRoleSets;
import org.sqlunet.pb.C.PbRoleSets_X;

import java.util.Arrays;

/**
 * PropBank provider
 *
 * @author <a href="mailto:1313ou@gmail.com">Bernard Bou</a>
 */
public class Q0 implements Q
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
				actualSelection += PbRoleSets.ROLESETID + " = ?";
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
				table = "pbrolesets " + //
						"LEFT JOIN pbrolesetmembers AS " + C.MEMBER + " USING (rolesetid) " + //
						"LEFT JOIN pbwords AS " + C.WORD + " ON " + C.MEMBER + ".pbwordid = " + C.WORD + ".pbwordid";
				break;

			case "WORDS_PBROLESETS":
				table = "words " + //
						"INNER JOIN pbwords USING (wordid) " + //
						"INNER JOIN pbrolesets USING (pbwordid)";
				break;

			case "PBROLESETS_PBROLES":
				table = "pbrolesets " + //
						"INNER JOIN pbroles USING (rolesetid) " + //
						"LEFT JOIN pbfuncs USING (func) " + //
						"LEFT JOIN pbvnthetas USING (theta)";
				actualSortOrder = "narg";
				break;

			case "PBROLESETS_PBEXAMPLES_BY_EXAMPLE":
				groupBy = C.EXAMPLE + ".exampleid";
				//$FALL-THROUGH$
				//noinspection fallthrough
			case "PBROLESETS_PBEXAMPLES":
				table = "pbrolesets " + //
						"INNER JOIN pbexamples AS " + C.EXAMPLE + " USING (rolesetid) " + //
						"LEFT JOIN pbrels AS " + C.REL + " USING (exampleid) " + //
						"LEFT JOIN pbargs AS " + C.ARG + " USING (exampleid) " + //
						"LEFT JOIN pbfuncs AS " + C.FUNC + " ON (" + C.ARG + ".func = " + C.FUNC + ".func) " + //
						"LEFT JOIN pbaspects USING (aspect) " + //
						"LEFT JOIN pbforms USING (form) " + //
						"LEFT JOIN pbtenses USING (tense) " + //
						"LEFT JOIN pbvoices USING (voice) " + //
						"LEFT JOIN pbpersons USING (person) " + //
						"LEFT JOIN pbroles USING (rolesetid,narg) " + //
						"LEFT JOIN pbvnthetas USING (theta)";
				actualSortOrder = C.EXAMPLE + ".exampleid,narg";
				break;

			// L O O K U P

			case "LOOKUP_FTS_EXAMPLES":
				table = "pbexamples_text_fts4";
				break;
			case "LOOKUP_FTS_EXAMPLES_X_BY_EXAMPLE":
				groupBy = "exampleid";
				//$FALL-THROUGH$
				//noinspection fallthrough
			case "LOOKUP_FTS_EXAMPLES_X":
				table = "pbexamples_text_fts4 " + //
						"LEFT JOIN pbrolesets USING (rolesetid)";
				break;

			// S U G G E S T

			case "SUGGEST_WORDS":
			{
				table = "pbwords";
				actualProjection = new String[]{"pbwordid AS _id", //
						"lemma AS " + "SearchManager.SUGGEST_COLUMN_TEXT_1", //
						"lemma AS " + "SearchManager.SUGGEST_COLUMN_QUERY"}; //
				actualSelection = "lemma LIKE ? || '%'";
				actualSelectionArgs = new String[]{last};
				break;
			}

			case "SUGGEST_FTS_WORDS":
			{
				table = "pbwords_word_fts4";
				actualProjection = new String[]{"pbwordid AS _id", //
						"lemma AS " + "SearchManager.SUGGEST_COLUMN_TEXT_1", //
						"lemma AS " + "SearchManager.SUGGEST_COLUMN_QUERY"}; //
				actualSelection = "lemma MATCH ?"; //
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
