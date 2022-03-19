/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package org.sqlunet.sn;

import org.sqlbuilder.common.Lib;
import org.sqlbuilder.common.Q;
import org.sqlunet.sn.C.SnCollocations;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * SyntagNet provider
 *
 * @author <a href="mailto:1313ou@gmail.com">Bernard Bou</a>
 */
public class QV implements Q
{
	//# instantiated at runtime
	static public final String URI_LAST = "#{uri_last}";

	@Override
	public String[] query(String keyname)
	{
		final String last = URI_LAST;

		String table;
		String[] projection = null;
		String selection = null;
		String[] selectionArgs = null;
		String groupBy = null;

		Key key = Key.valueOf(keyname);
		switch (key)
		{
			// I T E M
			// the incoming URI was for a single item because this URI was for a single row, the _ID value part is present.
			// get the last path segment from the URI: this is the _ID value. then, append the value to the WHERE clause for the query

			case COLLOCATIONS:
				table = SnCollocations.TABLE;
				if (selection != null)
				{
					selection += " AND ";
				}
				else
				{
					selection = "";
				}
				selection += SnCollocations.COLLOCATIONID + " = ?";
				break;

			// J O I N S

			case COLLOCATIONS_X:
				table = "syntagms " + //
						"JOIN words AS " + C.W1 + " ON (word1id = " + C.W1 + ".wordid) " + //
						"JOIN words AS " + C.W2 + " ON (word2id = " + C.W2 + ".wordid) " + //
						"JOIN synsets AS " + C.S1 + " ON (synset1id = " + C.S1 + ".synsetid) " + //
						"JOIN synsets AS " + C.S2 + " ON (synset2id = " + C.S2 + ".synsetid)";
				break;


			default:
				return null;
		}
		return new String[]{ //
				Lib.quote(table), //
				projection == null ? null : "{" + Arrays.stream(projection).map(Lib::quote).collect(Collectors.joining(",")) + "}", //
				Lib.quote(selection), //
				selectionArgs == null ? null : "{" + Arrays.stream(selectionArgs).map(Lib::quote).collect(Collectors.joining(",")) + "}", //
				Lib.quote(groupBy)};
	}

	public enum Key
	{
		COLLOCATIONS, COLLOCATIONS_X
	}
}
