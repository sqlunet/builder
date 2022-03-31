/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package org.sqlunet.sn;

import org.sqlbuilder.common.Lib;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * SyntagNet provider
 *
 * @author <a href="mailto:1313ou@gmail.com">Bernard Bou</a>
 */
public class QV implements Function<String,String[]>
{
	//# instantiated at runtime
	static public final String URI_LAST = "#{uri_last}";

	@Override
	public String[] apply(String keyname)
	{
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
				table = "${syntagms.table}";
				if (selection != null)
				{
					selection += " AND ";
				}
				else
				{
					selection = "";
				}
				selection += String.format("%s = ?", "${syntagms.syntagmid}");
				break;

			// J O I N S

			case COLLOCATIONS_X:
				table = String.format("%s " + //
								"JOIN %s AS %s ON (%s = %s.%s) " + //
								"JOIN %s AS %s ON (%s = %s.%s) " + //
								"JOIN %s AS %s ON (%s = %s.%s) " + //
								"JOIN %s AS %s ON (%s = %s.%s)", "${syntagms.table}", //
						"${wnwords.table}", "${as_words1}", "${syntagms.word1id}", "${as_words1}", "${wnwords.wordid}", //
						"${wnwords.table}", "${as_words2}", "${syntagms.word2id}", "${as_words2}", "${wnwords.wordid}", //
						"${wnsynsets.table}", "${as_synsets1}", "${syntagms.synset1id}", "${as_synsets1}", "${wnsynsets.synsetid}", //
						"${wnsynsets.table}", "${as_synsets2}", "${syntagms.synset2id}", "${as_synsets2}", "${wnsynsets.synsetid}");
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
