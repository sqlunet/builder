package org.sqlbuilder.vn;

import org.sqlbuilder.common.DeSerialize;
import org.sqlbuilder.common.Nullable;
import org.sqlbuilder.common.Resolver;

import java.io.File;
import java.io.IOException;

public class VnWordResolver extends Resolver<String,Integer>
{
	public VnWordResolver(final String ser) throws IOException, ClassNotFoundException
	{
		super(DeSerialize.deserialize(new File(ser)));
	}
}
