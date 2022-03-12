/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package org.sqlunet.bnc;

import org.sqlbuilder.common.Lib;
import org.sqlbuilder.common.Q;

import java.util.Arrays;
import java.util.stream.Collectors;

public class QV implements Q
{
	@Override
	public String[] query(String key)
	{
		final String last = "${uri_last}";
		final String[] projection = null;
		final String selection = null;
		final String[] selectionArgs = null;

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
				actualSelection += C.BNCs.POS + " = ?";
				break;

			// J O I N S

			case "WORDS_BNCS":
				table = String.format("%s " + //
								"LEFT JOIN %s USING (%s, %s) " + //
								"LEFT JOIN %s USING (%s, %s) " + //
								"LEFT JOIN %s USING (%s, %s) ", //
						"${bncs.table} ", //
						"${spwrs.table}", "${wnwords.wordid}", "${wnposes.posid}", //
						"${convtasks.table}", "${wnwords.wordid}", "${wnposes.posid}",  //
						"${imaginfs.table}", "${wnwords.wordid}", "${wnposes.posid}");
				break;

			default:
				return null;
		}
		return new String[]{ //
				Lib.quote(table), //
				actualProjection == null ? null : "{" + Arrays.stream(actualProjection).map(Lib::quote).collect(Collectors.joining(",")) + "}", //
				Lib.quote(actualSelection), //
				actualSelectionArgs == null ? null : "{" + Arrays.stream(actualSelectionArgs).map(Lib::quote).collect(Collectors.joining(",")) + "}", //
				Lib.quote(groupBy)};
	}
}
