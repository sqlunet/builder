package org.sqlbuilder.su;

import org.sqlbuilder.common.Resolver2;
import org.sqlbuilder2.ser.DeSerialize;

import java.io.File;
import java.io.IOException;

public class SuSynset31Resolver extends Resolver2<Character, Long, Long>
{
	public SuSynset31Resolver(final String ser) throws IOException, ClassNotFoundException
	{
		super(DeSerialize.deserialize(new File(ser)));
	}
}
