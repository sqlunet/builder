package org.sqlbuilder.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Update
{
	public static <T extends Resolvable<U, R>, U, R> void update(final Map<T, Integer> map, final File file, final String table,
			final Resolver<U, R> resolver,
			final Function<R,String> stringifier,
			final String resolvingColumn) throws FileNotFoundException
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
						String sqlResolved = stringifier.apply(resolved);
						String sqlResolving = resolving instanceof String ? Utils.quote(resolving.toString()) : resolving.toString();
						String comment = key.comment();
						String row = String.format("UPDATE %s SET %s WHERE %s=%s;", table, sqlResolved, resolvingColumn, sqlResolving);
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

	public static <T extends Resolvable<U, R>, U, R> void update(final Set<T> set, final File file, final String table,
			final Resolver<U, R> resolver,
			final Function<R,String> stringifier,
			final String resolvingColumn) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(file)))
		{
			ps.println("-- " + file.getName());
			if (set.size() > 0)
			{
				set.forEach(item -> {

					R resolved = item.resolve(resolver);
					if (resolved != null)
					{
						U resolving = item.resolving();
						String sqlResolved = stringifier.apply(resolved);
						String sqlResolving = resolving instanceof String ? Utils.quote(resolving.toString()) : resolving.toString();
						String comment = item.comment();
						String row = String.format("UPDATE %s SET %s WHERE %s=%s;", table, sqlResolved, resolvingColumn, sqlResolving);
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
