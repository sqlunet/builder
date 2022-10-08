package org.sqlbuilder.su;

import org.sqlbuilder.common.Resolver;
import org.sqlbuilder2.ser.DeSerialize;

import java.io.File;
import java.io.IOException;

public class SuSynsetResolver extends Resolver<String, Integer>
{
	public SuSynsetResolver(final String ser) throws IOException, ClassNotFoundException
	{
		super(DeSerialize.deserialize(new File(ser)));
	}
}
