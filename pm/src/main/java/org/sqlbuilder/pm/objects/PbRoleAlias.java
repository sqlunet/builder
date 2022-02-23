package org.sqlbuilder.pm.objects;

import org.sqlbuilder.common.Resolvable;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder2.ser.Pair;

public class PbRoleAlias implements Resolvable<Pair<String, String>, Pair<Integer, Integer>>
{
	public String roleset;

	public String arg;

	@Override
	public String dataRow()
	{
		return String.format("%s,%s", Utils.nullableQuotedEscapedString(roleset), Utils.nullableQuotedEscapedString(arg));
	}

	@Override
	public Pair<String, String> resolving()
	{
		return new Pair<>(roleset, arg);
	}
}
