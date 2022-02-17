package org.sqlbuilder.pb;

import org.sqlbuilder.common.DeSerialize;
import org.sqlbuilder.common.Nullable;
import org.sqlbuilder.common.Resolver;
import org.sqlbuilder2.ser.Pair;
import org.sqlbuilder2.ser.Triplet;

import java.io.File;
import java.io.IOException;

public class PbVnClassRoleResolver extends Resolver<Pair<String,String>, Triplet<Integer,Integer,Integer>>
{
	public PbVnClassRoleResolver(final String ser) throws IOException, ClassNotFoundException
	{
		super(DeSerialize.deserialize(new File(ser)));
	}
}
