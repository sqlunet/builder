package org.sqlbuilder.sumo;

import org.sqlbuilder.common.Resolver2;
import org.sqlbuilder2.ser.DeSerialize;

import java.io.File;
import java.io.IOException;

public class SuSynsetResolver extends Resolver2<Character, Long, Long>
{
	public SuSynsetResolver(final String ser) throws IOException, ClassNotFoundException
	{
		super(DeSerialize.deserialize(new File(ser)));
	}
}
