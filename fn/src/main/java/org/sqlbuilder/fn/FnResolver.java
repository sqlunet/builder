package org.sqlbuilder.fn;

import org.sqlbuilder.common.DeSerialize;
import org.sqlbuilder.common.Nullable;
import org.sqlbuilder.common.Resolver;

import java.io.File;
import java.io.IOException;

public class FnResolver extends Resolver<String,Integer>
{
	public FnResolver(final String ser) throws IOException, ClassNotFoundException
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
