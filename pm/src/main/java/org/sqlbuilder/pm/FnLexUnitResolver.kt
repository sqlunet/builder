package org.sqlbuilder.pm;

import org.sqlbuilder.common.DeSerialize;
import org.sqlbuilder.common.Resolver;
import org.sqlbuilder2.ser.Pair;
import org.sqlbuilder2.ser.Triplet;

import java.io.File;
import java.io.IOException;

public class FnLexUnitResolver extends Resolver<Pair<String, String>, Pair<Integer, Integer>>
{
	public FnLexUnitResolver(final String ser) throws IOException, ClassNotFoundException
	{
		super(DeSerialize.deserialize(new File(ser)));
	}
}
