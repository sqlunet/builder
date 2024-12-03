package org.sqlbuilder.su;

import org.sqlbuilder.common.DeSerialize;
import org.sqlbuilder.common.Resolver;

import java.io.File;
import java.io.IOException;

public class SuWordResolver extends Resolver<String, Integer>
{
	public SuWordResolver(final String ser) throws IOException, ClassNotFoundException
	{
		super(DeSerialize.deserialize(new File(ser)));
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException
	{
		var r = new SuWordResolver(args[0]);
		var id = r.apply("airport");
		System.out.println(id);
	}
}
