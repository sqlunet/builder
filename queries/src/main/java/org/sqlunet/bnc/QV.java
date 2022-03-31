/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package org.sqlunet.bnc;

import org.sqlbuilder.common.Lib;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public class QV implements Function<String,String[]>
{
	@Override
	public String[] apply(String keyname)
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

		Key key = Key.valueOf(keyname);
		switch (key)
		{
			// I T E M
			// the incoming URI was for a single item because this URI was for a single row, the _ID value part is present.
			// get the last path segment from the URI: this is the _ID value. then, append the value to the WHERE clause for the query

			case BNCS:
				table = "${bncs.table}";
				if (actualSelection != null)
				{
					actualSelection += " AND ";
				}
				else
				{
					actualSelection = "";
				}
				actualSelection += String.format("%s = ?", "${bncs.posid}");
				break;

			// J O I N S

			case WORDS_BNCS:
				table = String.format("%s " + //
								"LEFT JOIN %s USING (%s, %s) " + //
								"LEFT JOIN %s USING (%s, %s) " + //
								"LEFT JOIN %s USING (%s, %s) ", //
						"${bncs.table}", //
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

	public enum Key
	{
		BNCS, WORDS_BNCS;
	}
}
