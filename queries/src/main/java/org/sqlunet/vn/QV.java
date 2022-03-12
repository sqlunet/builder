/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package org.sqlunet.vn;

import org.sqlbuilder.common.Lib;
import org.sqlbuilder.common.Q;
import org.sqlunet.vn.C.*;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * VerbNet provider
 *
 * @author <a href="mailto:1313ou@gmail.com">Bernard Bou</a>
 */
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

			case "VNCLASS":
				table = "${vnclasses.table}";
				if (actualSelection != null)
				{
					actualSelection += " AND ";
				}
				else
				{
					actualSelection = "";
				}
				actualSelection += "${vnclasses.classid}" + " = " + "URI_PATH_SEGMENT";
				break;

			case "VNCLASSES":
				table = "${vnclasses.table}";
				break;

			case "VNCLASSES_X_BY_VNCLASS":
				table = String.format("%s " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s)", //
						"${vnclasses.table}", //
						"${vnmembers_vngroupings.table}", "${vnclasses.classid}", //
						"${vngroupings.table}", "${vngroupings.groupingid}"); //
				groupBy = "${vnclasses.classid}";
				break;

			// J O I N S

			case "WORDS_VNCLASSES":
				table = String.format("%s " + //
								"INNER JOIN %s USING (%s) " + //
								"INNER JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s)", //
						"${words.table}", //
						"${vnwords.table}", "${words.wordid}", //
						"${vnmembers_senses.table}", "${vnwords.vnwordid}", //
						"${vnclasses.table}", "${vnclasses.classid}");
				break;

			case "VNCLASSES_VNMEMBERS_X_BY_WORD":
				table = String.format("%s " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s, %s) " + //
								"LEFT JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s)", //
						"${vnmembers_senses.table}", //
						"${vnwords.table}", "${vnwords.vnwordid}", //
						"${vnmembers_vngroupings.table}", "${vnmembers.classid}", "${vnmembers.vnwordid}", //
						"${vngroupings.table}", "${vngroupings.groupingid}", //
						"${synsets.table}", "${synsets.synsetid}");
				groupBy = "${vnwords.vnwordid}";
				break;

			case "VNCLASSES_VNROLES_X_BY_VNROLE":
				table = String.format("%s " + //
								"INNER JOIN %s USING (%s) " + //
								"INNER JOIN %s USING (%s) " + //
								"LEFT JOIN %s USING (%s)", //
						"${vnclasses_vnroles.table}", //
						"${vnroles.table}", "${vnroles.roleid}", //
						"${vnroletypes.table}", "${vnroletypes.roletypeid}", //
						"${vnrestrs.table}", "${vnrestrs.restrsid}");
				groupBy = "${vnroles.roleid}";
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
						"${vnclasses_vnframes.table}", //
						"${vnframes.table}", "${vnframes.frameid}", //
						"${vnframenames.table}", "${vnframenames.framenameid}", //
						"${vnframesubnames.table}", "${vnframesubnames.framesubnameid}", //
						"${vnsyntaxes.table}", "${vnsyntaxes.syntaxid}", //
						"${vnsemantics.table}", "${vnsemantics.semanticsid}", //
						"${vnframes_vnexamples.table}", "${vnframes.frameid}", //
						"${vnexamples.table}", "${vnexamples.exampleid}");
				groupBy = "${vnframes.frameid}";
				break;

			// L O O K U P

			case "LOOKUP_FTS_EXAMPLES":
				table = String.format("%s_%s_fts4", "${vnexamples.table}", "${vnexamples.example}");
				break;

			case "LOOKUP_FTS_EXAMPLES_X_BY_EXAMPLE":
				groupBy = "${vnexamples.exampleid}";
				//$FALL-THROUGH$
				//noinspection fallthrough
			case "LOOKUP_FTS_EXAMPLES_X":
				table = String.format("%s_%s_fts4 " + //
								"LEFT JOIN %s USING (%s)", //
						"${vnexamples.table}", "${vnexamples.example}", //
						"${vnclasses.table}", "${vnclasses.classid}");
				break;

			// S U G G E S T

			case "SUGGEST_WORDS":
			{
				table = "${vnwords.table}";
				actualProjection = new String[]{String.format("%s AS _id", "${vnwords.vnwordid}"), //
						String.format("%s AS " + "SearchManager.SUGGEST_COLUMN_TEXT_1", "${vnwords.word}"), //
						String.format("%s AS " + "SearchManager.SUGGEST_COLUMN_QUERY", "${vnwords.word}")}; //
				actualSelection = String.format("%s LIKE ? || '%%'", "${vnwords.word}");
				actualSelectionArgs = new String[]{last};
				break;
			}

			case "SUGGEST_FTS_WORDS":
			{
				table = String.format("%s_%s_fts4", "${vnwords.table}", "${vnwords.word}");
				actualProjection = new String[]{String.format("%s AS _id", "${vnwords.vnwordid}"),//
						String.format("%s AS " + "SearchManager.SUGGEST_COLUMN_TEXT_1", "${vnwords.word}"), //
						String.format("%s AS " + "SearchManager.SUGGEST_COLUMN_QUERY", "${vnwords.word}")}; //
				actualSelection = String.format("%s MATCH ?", "${vnwords.word}");
				actualSelectionArgs = new String[]{last + '*'};
				break;
			}

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