package org.sqlbuilder.bnc;

import org.sqlbuilder.common.DeSerialize;
import org.sqlbuilder.common.Resolver;

import java.io.File;
import java.io.IOException;

public class BncWordResolver extends Resolver<String,Integer>
{
	public BncWordResolver(final String ser) throws IOException, ClassNotFoundException
	{
		super(DeSerialize.deserialize(new File(ser)));
	}
}
