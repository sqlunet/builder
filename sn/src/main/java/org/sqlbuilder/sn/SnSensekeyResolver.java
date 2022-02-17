package org.sqlbuilder.sn;

import org.sqlbuilder.common.Nullable;
import org.sqlbuilder.common.Resolver;
import org.sqlbuilder2.ser.DeSerialize;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;

public class SnSensekeyResolver extends Resolver<String, SimpleEntry<Integer, Integer>>
{
	public SnSensekeyResolver(final String ser) throws IOException, ClassNotFoundException
	{
		super(DeSerialize.deserialize(new File(ser)));
	}
}
