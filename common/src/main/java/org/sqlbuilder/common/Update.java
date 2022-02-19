package org.sqlbuilder.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Update
{
	public static <T extends Resolvable<U, R>, U, R> void update(final Collection<T> collection, final File file, final String table,
			final Function<U, R> resolver,
			final Function<R,String> stringifier,
			final String... resolvingColumn) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(file)))
		{
			ps.println("-- " + file.getName());
			if (collection.size() > 0)
			{
				collection.forEach(item -> {

					R resolved = item.resolve(resolver);
					if (resolved != null)
					{
						U resolving = item.resolving();
						String sqlResolved = stringifier.apply(resolved);
						String sqlResolving = resolving instanceof String ? Utils.quote(resolving.toString()) : resolving.toString();
						String comment = item.comment();
						String row = String.format("UPDATE %s SET %s WHERE %s=%s;", table, sqlResolved, resolvingColumn[0], sqlResolving);
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

	public static <T extends Resolvable<U, R>, U, R> void updateResolvable(final Collection<T> collection, final File file, final String table, //
			final Function<U, R> resolver, //
			final Function<R,String> setStringifier, //
			final Function<U,String> whereStringifier //
	) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(file)))
		{
			ps.println("-- " + file.getName());
			if (collection.size() > 0)
			{
				collection.forEach(item -> {

					R resolved = item.resolve(resolver);
					if (resolved != null)
					{
						U resolving = item.resolving();
						String sqlResolving = whereStringifier.apply(resolving);
						String sqlResolved = setStringifier.apply(resolved);
						String comment = item.comment();
						String row = String.format("UPDATE %s SET %s WHERE %s;", table, sqlResolved, sqlResolving);
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
