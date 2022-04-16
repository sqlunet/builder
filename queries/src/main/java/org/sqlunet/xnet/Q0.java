/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package org.sqlunet.xnet;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Extended cross WordNet-FrameNet-PropBank-VerbNet provider
 *
 * @author <a href="mailto:1313ou@gmail.com">Bernard Bou</a>
 */
public class Q0 implements Function<String, String[]>, Supplier<String[]>
{

	static public final String WORD = "w";
	static public final String SENSE = "s";
	static public final String SYNSET = "y";
	static public final String POSID = "p";
	static public final String CLASS = "c";

	// C O N S T R U C T O R

	/**
	 * Constructor
	 */
	public Q0()
	{
	}

	// Q U E R Y

	private static String quote(String str)
	{
		return str == null ? null : String.format("\"%s\"", str);
	}

	@Override
	public String[] apply(final String keyname)
	{
		String table = null;
		String[] projection = null;
		String selection = null;
		String[] selectionArgs = null;
		String groupBy = null;
		String sortOrder = null;

		Key key = Key.valueOf(keyname);
		switch (key)
		{
			case PREDICATEMATRIX:
				// table = "pm";
				table = "pmvn " + //
						"LEFT JOIN pnpb USING (wordid)" + //
						"LEFT JOIN pnfn USING (wordid)" //
				;
				break;

			case PREDICATEMATRIX_VERBNET:
				table = "pmvn";
				break;

			case PREDICATEMATRIX_PROPBANK:
				table = "pmpb";
				break;

			case PREDICATEMATRIX_FRAMENET:
				table = "pmfn";
				break;

			case SOURCES:
				table = "sources";
				break;

			// J O I N S

			case WORDS_FNWORDS_PBWORDS_VNWORDS:
				table = "words AS " + WORD + ' ' + //
						"LEFT JOIN senses AS " + SENSE + " USING (wordid) " + //
						"LEFT JOIN synsets AS " + SYNSET + " USING (synsetid) " + //
						"LEFT JOIN poses AS " + POSID + " USING (posid) " + //
						"LEFT JOIN casedwords USING (wordid,casedwordid) " + //
						"LEFT JOIN domains USING (domainid) " + //
						"LEFT JOIN fn_words USING (wordid) " + //
						"LEFT JOIN vn_words USING (wordid) " + //
						"LEFT JOIN pb_words USING (wordid)";
				groupBy = "synsetid";
				break;

			case WORDS_PBWORDS_VNWORDS:
				table = "words AS " + WORD + ' ' + //
						"LEFT JOIN senses AS " + SENSE + " USING (wordid) " + //
						"LEFT JOIN synsets AS " + SYNSET + " USING (synsetid) " + //
						"LEFT JOIN poses AS " + POSID + " USING (posid) " + //
						"LEFT JOIN casedwords USING (wordid,casedwordid) " + //
						"LEFT JOIN domains USING (domainid) " + //
						"LEFT JOIN vn_words USING (wordid) " + //
						"LEFT JOIN pb_words USING (wordid)";
				groupBy = "synsetid";
				break;

			case WORDS_VNWORDS_VNCLASSES:
			{
				table = "vn_words " + //
						"INNER JOIN vn_members_senses USING (wordid) " + //
						"INNER JOIN vn_classes AS " + CLASS + " USING (classid) " + //
						"LEFT JOIN synsets USING (synsetid)";
				groupBy = "wordid,synsetid,classid";
				break;
			}

			case WORDS_VNWORDS_VNCLASSES_U:
			{
				final String table1 = "pmvn " + //
						"INNER JOIN vn_classes USING (classid) " + //
						"LEFT JOIN synsets USING (synsetid)";
				final String table2 = "vn_words " + //
						"INNER JOIN vn_members_senses USING (vnwordid) " + //
						"INNER JOIN vn_classes USING (classid)";
				final String[] unionProjection = {"wordid", "synsetid", "classid", "class", "classtag", "definition"};
				final String[] table1Projection = unionProjection;
				final String[] table2Projection = {"wordid", "synsetid", "classid", "class", "classtag"};
				final String[] groupByArray = {"wordid", "synsetid", "classid"};
				assert projection != null;
				final String query = makeQuery(table1, table2, table1Projection, table2Projection, unionProjection, projection, selection, groupByArray, sortOrder, "vn");
				break;
			}

			case WORDS_PBWORDS_PBROLESETS:
			{
				table = "pb_words " + //
						"INNER JOIN pb_rolesets AS " + CLASS + " USING (pbwordid)";
				groupBy = "wordid,synsetid,rolesetid";
				break;
			}

			case WORDS_PBWORDS_PBROLESETS_U:
			{
				final String table1 = "pmpb " + //
						"INNER JOIN pb_rolesets USING (rolesetid) " + //
						"LEFT JOIN synsets USING (synsetid)";
				final String table2 = "pb_words " + //
						"INNER JOIN pb_rolesets USING (pbwordid)";
				final String[] unionProjection = {"wordid", "synsetid", "rolesetid", "rolesetname", "rolesethead", "rolesetdescr", "definition"};
				final String[] table1Projection = unionProjection;
				final String[] table2Projection = {"wordid", "rolesetid", "rolesetname", "rolesethead", "rolesetdescr"};
				final String[] groupByArray = {"wordid", "synsetid", "rolesetid"};
				assert projection != null;
				final String query = makeQuery(table1, table2, table1Projection, table2Projection, unionProjection, projection, selection, groupByArray, sortOrder, "pb");
				break;
			}

			case WORDS_FNWORDS_FNFRAMES_U:
			{
				final String table1 = "pmfn " + //
						"INNER JOIN fn_frames USING (frameid) " + //
						"LEFT JOIN fn_lexunits USING (luid,frameid) " + //
						"LEFT JOIN synsets USING (synsetid)";
				final String table2 = "fnwords " + //
						"INNER JOIN fn_lexemes USING (fnwordid) " + //
						"INNER JOIN fn_lexunits USING (luid,posid) " + //
						"INNER JOIN fn_frames USING (frameid)";
				final String[] unionProjection = {"wordid", "synsetid", "frameid", "frame", "framedefinition", "luid", "lexunit", "ludefinition", "definition"};
				final String[] table1Projection = unionProjection;
				final String[] table2Projection = {"wordid", "frameid", "frame", "framedefinition", "luid", "lexunit", "ludefinition"};
				final String[] groupByArray = {"wordid", "synsetid", "frameid"};
				assert projection != null;
				final String query = makeQuery(table1, table2, table1Projection, table2Projection, unionProjection, projection, selection, groupByArray, sortOrder, "fn");
				break;
			}

			default:
				return null;
		}
		return new String[]{ //
				quote(table), //
				projection == null ? null : "{" + Arrays.stream(projection).map(Q0::quote).collect(Collectors.joining(",")) + "}", //
				quote(selection), //
				selectionArgs == null ? null : "{" + Arrays.stream(selectionArgs).map(Q0::quote).collect(Collectors.joining(",")) + "}", //
				quote(groupBy), //
				quote(sortOrder)};
	}

	/**
	 * Make union query
	 *
	 * @param table1           table1
	 * @param table2           table2
	 * @param table1Projection table1 projection
	 * @param table2Projection table2 projection
	 * @param unionProjection  union projection
	 * @param projection       final projection
	 * @param selection        selection
	 * @param groupBys         group by
	 * @param sortOrder        sort
	 * @param tag              tag
	 * @return union sql
	 */
	private String makeQuery(final String table1, final String table2, //
			final String[] table1Projection, final String[] table2Projection, //
			final String[] unionProjection, final String[] projection, //
			final String selection, //
			final String[] groupBys, final String sortOrder, final String tag)
	{
		return "#{query}";
	}

	@Override
	public String[] get()
	{
		return Arrays.stream(Key.values()).map(Enum::name).toArray(String[]::new);
	}

	private enum Key
	{
		PREDICATEMATRIX, //
		PREDICATEMATRIX_VERBNET, //
		PREDICATEMATRIX_PROPBANK, //
		PREDICATEMATRIX_FRAMENET, //
		WORDS_FNWORDS_PBWORDS_VNWORDS, //

		WORDS_PBWORDS_VNWORDS, //
		WORDS_VNWORDS_VNCLASSES, //
		WORDS_VNWORDS_VNCLASSES_U, //
		WORDS_PBWORDS_PBROLESETS, //
		WORDS_PBWORDS_PBROLESETS_U, //
		WORDS_FNWORDS_FNFRAMES_U, //

		SOURCES,
	}
}
