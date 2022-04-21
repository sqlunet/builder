/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package org.sqlunet.pm;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * PredicateMatrix provider
 *
 * @author <a href="mailto:1313ou@gmail.com">Bernard Bou</a>
 */
public class Q0 implements Function<String, String[]>, Supplier<String[]>
{
	static public final String TABLE = "pm";

	static public final String PMROLE = "mr";
	static public final String PMPREDICATE = "mp";
	static public final String VNCLASS = "vc";
	static public final String VNROLE = "vr";
	static public final String VNROLETYPE = "vt";
	static public final String PBROLESET = "pc";
	static public final String PBROLE = "pr";
	static public final String PBARG = "pa";
	static public final String FNFRAME = "ff";
	static public final String FNFE = "fe";
	static public final String FNFETYPE = "ft";
	static public final String FNLU = "fu";

	// C O N S T R U C T O R

	/**
	 * Constructor
	 */
	public Q0()
	{
	}

	@Override
	public String[] apply(final String keyname)
	{
		String table;
		String[] projection = null;
		String selection = null;
		String[] selectionArgs = null;
		String groupBy = null;
		String sortOrder = null;

		Key key = Key.valueOf(keyname);
		switch (key)
		{
			case PM:
				table = TABLE;
				break;

			case PM_X:
				table = "pm " + //
						"LEFT JOIN pmroles AS " + PMROLE + " USING (pmroleid) " + //
						"LEFT JOIN pmpredicates AS " + PMPREDICATE + " USING (pmpredicateid) " + //
						"LEFT JOIN synsets USING (synsetid) " + //
						"LEFT JOIN vnclasses AS " + VNCLASS + " ON vnclassid = " + VNCLASS + ".classid " + //
						"LEFT JOIN vnroles AS " + VNROLE + " ON vnroleid = " + VNROLE + ".roleid " + //
						"LEFT JOIN vnroletypes AS " + VNROLETYPE + " ON " + VNROLE + ".roletypeid = " + VNROLETYPE + ".roletypeid " + //
						"LEFT JOIN pbrolesets AS " + PBROLESET + " ON pbrolesetid = " + PBROLESET + ".rolesetid " + //
						"LEFT JOIN pbroles AS " + PBROLE + " ON pbroleid = " + PBROLE + ".roleid " + //
						"LEFT JOIN pbargtypes AS " + PBARG + " ON " + PBROLE + ".argtypeid = " + PBARG + ".argtypeid " + //
						"LEFT JOIN fnframes AS " + FNFRAME + " ON fnframeid = " + FNFRAME + ".frameid " + //
						"LEFT JOIN fnfes AS " + FNFE + " ON fnfeid = " + FNFE + ".feid " + //
						"LEFT JOIN fnfetypes AS " + FNFETYPE + " ON " + FNFE + ".fetypeid = " + FNFETYPE + ".fetypeid " + //
						"LEFT JOIN fnlexunits AS " + FNLU + " ON fnluid = " + FNLU + ".luid";
				break;

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

	private enum Key
	{
		PM, PM_X
	}

	@Override
	public String[] get()
	{
		return Arrays.stream(Key.values()).map(Enum::name).toArray(String[]::new);
	}


	private static String quote(String str)
	{
		return str == null ? null : String.format("\"%s\"", str);
	}
}
