package org.sqlbuilder.pb;

import org.sqlbuilder.common.DeSerialize;
import org.sqlbuilder.common.Nullable;
import org.sqlbuilder.common.Resolver;

import java.io.File;
import java.io.IOException;

public class PbVnRoleResolver extends Resolver<String,Integer>
{
	public PbVnRoleResolver(final String ser) throws IOException, ClassNotFoundException
	{
		super(DeSerialize.deserialize(new File(ser)));
	}

	@Nullable
	@Override
	public Integer apply(final String k)
	{
		return map.get(k);
	}
}
