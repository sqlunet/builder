package org.sqlbuilder.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Map;

public class Update
{
	public static <T extends Resolvable<U, R>, U, R> void update(final Map<T, Integer> map, final File file, final String table, final Resolver<U, R> resolver, final String resolvingColumn, final String resolvedColumn) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(file)))
		{
			ps.println("-- " + file.getName());
			if (map.size() > 0)
			{
				map.forEach((key, idx) -> {

					R resolved = key.resolve(resolver);
					if (resolved != null)
					{
						U resolving = key.resolving();
						String sqlResolved = Utils.nullable(resolved, Object::toString);
						String sqlResolving = resolving instanceof String ? Utils.quote(resolving.toString()) : resolving.toString();
						String comment = key.comment();
						String row = String.format("UPDATE %s SET %s=%s WHERE %s=%s;", table, resolvedColumn, sqlResolved, resolvingColumn, sqlResolving);
						if (comment != null)
						{
							row = String.format("%s /* %s */", row, comment);
						}
						ps.println(row);
					}
				});
			}
		}
	}
}
