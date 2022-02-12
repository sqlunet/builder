package org.sqlbuilder.sn;

import org.sqlbuilder.common.Nullable;
import org.sqlbuilder.common.Resolver;
import org.sqlbuilder2.legacy.DeSerialize;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Properties;

public class SnResolver extends Resolver<SimpleEntry<Integer, Integer>>
{
	public SnResolver(final Properties conf) throws IOException, ClassNotFoundException
	{
		super(DeSerialize.deserialize(new File(conf.getProperty("sense_nids"))));
	}

	@Nullable
	@Override
	public SimpleEntry<Integer, Integer> apply(final String sk)
	{
		return map.get(sk);
	}
}
