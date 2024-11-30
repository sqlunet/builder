package org.sqlbuilder.vn;

import org.sqlbuilder.common.DeSerialize;
import org.sqlbuilder.common.Resolver;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;

public class VnSensekeyResolver extends Resolver<String, SimpleEntry<Integer, Integer>>
{
	public VnSensekeyResolver(final String ser) throws IOException, ClassNotFoundException
	{
		super(DeSerialize.deserialize(new File(ser)));
	}
}
