package org.sqlbuilder.pm;

import org.sqlbuilder.common.DeSerialize;
import org.sqlbuilder.common.Resolver;
import org.sqlbuilder2.ser.Pair;
import org.sqlbuilder2.ser.Triplet;

import java.io.File;
import java.io.IOException;

public class FnRoleResolver extends Resolver<Pair<String, String>, Triplet<Integer, Integer, Integer>>
{
	public FnRoleResolver(final String ser) throws IOException, ClassNotFoundException
	{
		super(DeSerialize.deserialize(new File(ser)));
	}
}
