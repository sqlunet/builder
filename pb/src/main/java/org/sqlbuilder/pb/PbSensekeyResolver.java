package org.sqlbuilder.pb;

import org.sqlbuilder.common.DeSerialize;
import org.sqlbuilder.common.Nullable;
import org.sqlbuilder.common.Resolver;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;

public class PbSensekeyResolver extends Resolver<String, SimpleEntry<Integer, Integer>>
{
	public PbSensekeyResolver(final String ser) throws IOException, ClassNotFoundException
	{
		super(DeSerialize.deserialize(new File(ser)));
	}
}
