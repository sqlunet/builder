package org.sqlbuilder.pm;

import org.sqlbuilder.common.DeSerialize;
import org.sqlbuilder.common.Resolver;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;

public class SensekeyResolver extends Resolver<String, SimpleEntry<Integer, Integer>>
{
	public SensekeyResolver(final String ser) throws IOException, ClassNotFoundException
	{
		super(DeSerialize.deserialize(new File(ser)));
	}
}
