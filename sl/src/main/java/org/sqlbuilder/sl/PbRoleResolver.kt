package org.sqlbuilder.sl;

import org.sqlbuilder.common.DeSerialize;
import org.sqlbuilder.common.Resolver;
import org.sqlbuilder2.ser.Pair;

import java.io.File;
import java.io.IOException;

public class PbRoleResolver extends Resolver<Pair<String, String>, Pair<Integer, Integer>>
{
	public PbRoleResolver(final String ser) throws IOException, ClassNotFoundException
	{
		super(DeSerialize.deserialize(new File(ser)));
	}
}
