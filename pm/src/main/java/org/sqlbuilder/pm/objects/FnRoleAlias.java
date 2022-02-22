package org.sqlbuilder.pm.objects;

import org.sqlbuilder.common.Resolvable;
import org.sqlbuilder.common.Utils;
import org.sqlbuilder2.ser.Triplet;

public class FnRoleAlias implements Resolvable<Triplet<String,String,String>, Triplet<Integer,Integer,Integer>>
{
	public String frame;

	public String fetype;

	public String lu;

	@Override
	public String dataRow()
	{
		return String.format("'%s',%s,%s",frame, Utils.nullableQuotedEscapedString(fetype), Utils.nullableQuotedEscapedString(lu));
	}

	@Override
	public Triplet<String,String, String> resolving()
	{
		return new Triplet<>(frame, fetype, lu);
	}
}
