package org.sqlbuilder.pm.objects;

import org.sqlbuilder.common.Resolvable;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder2.ser.Pair;

public class VnRoleAlias implements Resolvable<Pair<String, String>, Pair<Integer, Integer>>
{
	public String clazz;

	public String role;

	@Override
	public String dataRow()
	{
		return String.format("%s,%s", Utils.nullableQuotedEscapedString(clazz), Utils.nullableQuotedEscapedString(role));
	}

	@Override
	public Pair<String, String> resolving()
	{
		return new Pair<>(clazz, role);
	}
}
