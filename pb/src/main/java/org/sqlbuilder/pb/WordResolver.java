package org.sqlbuilder.pb;

import org.sqlbuilder.common.DeSerialize;
import org.sqlbuilder.common.Resolver;

import java.io.File;
import java.io.IOException;

public class WordResolver extends Resolver<String,Integer>
{
	public WordResolver(final String ser) throws IOException, ClassNotFoundException
	{
		super(DeSerialize.deserialize(new File(ser)));
	}
}
