/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package org.sqlunet.sn;

import org.sqlunet.sn.C.SnCollocations;

import java.util.Arrays;
import java.util.function.Function;

/**
 * SyntagNet provider
 *
 * @author <a href="mailto:1313ou@gmail.com">Bernard Bou</a>
 */
public class Q0 implements Function<String, String[]>
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

			case "COLLOCATIONS":
				table = SnCollocations.TABLE;
				if (actualSelection != null)
				{
					actualSelection += " AND ";
				}
				else
				{
					actualSelection = "";
				}
				actualSelection += SnCollocations.COLLOCATIONID + " = ?";
				break;

			// J O I N S

			case "COLLOCATIONS_X":
				table = "syntagms " + //
						"JOIN words AS " + C.W1 + " ON (word1id = " + C.W1 + ".wordid) " + //
						"JOIN words AS " + C.W2 + " ON (word2id = " + C.W2 + ".wordid) " + //
						"JOIN synsets AS " + C.S1 + " ON (synset1id = " + C.S1 + ".synsetid) " + //
						"JOIN synsets AS " + C.S2 + " ON (synset2id = " + C.S2 + ".synsetid)";
				break;


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
