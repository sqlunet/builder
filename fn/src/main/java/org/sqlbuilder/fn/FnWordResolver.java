package org.sqlbuilder.fn;

import org.sqlbuilder.common.DeSerialize;
import org.sqlbuilder.common.Nullable;
import org.sqlbuilder.common.Resolver;

import java.io.File;
import java.io.IOException;

public class FnWordResolver extends Resolver<String,Integer>
{
	public FnWordResolver(final String ser) throws IOException, ClassNotFoundException
	{
		super(DeSerialize.deserialize(new File(ser)));
	}
}
