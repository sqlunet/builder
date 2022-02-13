package org.sqlbuilder.bnc;

import org.sqlbuilder.common.DeSerialize;
import org.sqlbuilder.common.Nullable;
import org.sqlbuilder.common.Resolver;

import java.io.File;
import java.io.IOException;

public class BncResolver extends Resolver<String,Integer>
{
	public BncResolver(final String ser) throws IOException, ClassNotFoundException
	{
		super(DeSerialize.deserialize(new File(ser)));
	}

	@Nullable
	@Override
	public Integer apply(final String sk)
	{
		return map.get(sk);
	}
}
