/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package org.sqlunet.bnc;

import java.util.Arrays;
import java.util.function.Function;

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

			case "BNCS":
				table = C.BNCs.TABLE;
				if (actualSelection != null)
				{
					actualSelection += " AND ";
				}
				else
				{
					actualSelection = "";
				}
				actualSelection += C.BNCs.POSID + " = ?";
				break;

			// J O I N S

			case "WORDS_BNCS":
				table = String.format("%s " + //
								"LEFT JOIN %s USING (%s, %s) " + //
								"LEFT JOIN %s USING (%s, %s) " + //
								"LEFT JOIN %s USING (%s, %s) ", //
						"bnc.bncs ", //
						"bnc.spwrse", "words.wordid", "bnc.posid", //
						"bnc.convtasks", "words.wordid", "bnc.posid",  //
						"bnc.imaginfs", "words.wordid", "bnc.posid");
				break;

			default:
				return null;
		}
		return new String[]{"T:" + table, //
				"P:" + Arrays.toString(actualProjection), //
				"S:" + actualSelection, //
				"A:" + Arrays.toString(actualSelectionArgs), //
				"G:" + groupBy};
	}
}
