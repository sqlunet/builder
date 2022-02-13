package org.sqlbuilder.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Map;

public class Update
{
	public static <T extends Resolvable<U,R>, U, R> void update(final Map<T, Integer> map, final File file, final String table, final Resolver<U,R> resolver, final String resolvingColumn, final String resolvedColumn) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(file)))
		{
			ps.println("-- " + file.getName());
			if (map.size() > 0)
			{
				map.forEach((key, idx) -> {

					U resolving = key.resolving();
					R resolved = key.resolve(resolver);
					String sqlResolved = Utils.nullable(resolved, Object::toString);
					String comment = key.comment();
					String row = String.format("UPDATE %s SET %s=%s WHERE %s = %s%n", table, resolvedColumn, sqlResolved, resolvingColumn, resolving);
					if (comment != null)
					{
						row = String.format("%s /* %s */", row, comment);
					}
					ps.println(row);
				});
			}
		}
	}
}
