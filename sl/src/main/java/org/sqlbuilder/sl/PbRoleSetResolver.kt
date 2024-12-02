package org.sqlbuilder.sl;

import org.sqlbuilder.common.DeSerialize;
import org.sqlbuilder.common.Resolver;

import java.io.File;
import java.io.IOException;

public class PbRoleSetResolver extends Resolver<String, Integer>
{
	public PbRoleSetResolver(final String ser) throws IOException, ClassNotFoundException
	{
		super(DeSerialize.deserialize(new File(ser)));
	}
}
