package org.sqlbuilder.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.StreamSupport;

public class Insert
{
	// I N S E R T A B L E S

	public static <T extends Insertable> void insert2(
			final Iterable<T> items,
			final File file,
			final String table,
			final String columns,
			final String header
	) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(file)))
		{
			ps.println("-- " + header);
			if (items.iterator().hasNext())
			{
				ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
				int[] i = {0};
				items.forEach(item -> {
					if (i[0] != 0)
					{
						ps.print(",\n");
					}
					String values = item.dataRow();
					String comment = item.comment();
					String row = comment != null ? String.format("(%s) /* %s */", values, comment) : String.format("(%s)", values);
					ps.print(row);
					i[0]++;
				});
				ps.println(";");
			}
		}
	}

	public static <T extends Insertable> void insert2(
			final Iterable<T> items,
			final Comparator<T> comparator,
			final File file,
			final String table,
			final String columns,
			final String header
	) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(file)))
		{
			ps.println("-- " + header);
			if (items.iterator().hasNext())
			{
				ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
				var stream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(items.iterator(), Spliterator.ORDERED), false);
				if (comparator != null)
				{
					stream = stream.sorted(comparator);
				}
				int[] i = {0};
				stream.forEach(e -> {
					if (i[0] != 0)
					{
						ps.print(",\n");
					}
					String values = e.dataRow();
					String comment = e.comment();
					String row = comment != null ? String.format("(%s) /* %s */", values, comment) : String.format("(%s)", values);
					ps.print(row);
					i[0]++;
				});
				ps.println(";");
			}
		}
	}

	public static <T extends Insertable> void insert2(
			final Iterable<T> items,
			final Function<T, Integer> resolver,
			final File file,
			final String table,
			final String columns,
			final String header
	) throws FileNotFoundException
	{
		insert2(items, resolver, file, table, columns, header, true);
	}

	public static <T extends Insertable> void insert2(
			final Iterable<T> items,
			final Function<T, Integer> resolver,
			final File file,
			final String table,
			final String columns,
			final String header,
			boolean withNumber
	) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(file)))
		{
			ps.println("-- " + header);
			if (items.iterator().hasNext())
			{
				ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
				int[] i = {0};
				items.forEach((key) -> {
					if (i[0] != 0)
					{
						ps.print(",\n");
					}
					var id = resolver.apply(key);
					String values = key.dataRow();
					String comment = key.comment();
					String row = withNumber ?
							(comment != null ? String.format("(%d,%s) /* %s */", id, values, comment) : String.format("(%d,%s)", id, values)) :
							(comment != null ? String.format("(%s) /* %s */", values, comment) : String.format("(%s)", values));
					ps.print(row);
					i[0]++;
				});
				ps.println(";");
			}
		}
	}

	public static <T extends Insertable> void insertAndIncrement2(
			final Iterable<T> items,
			final Comparator<T> comparator,
			final File file,
			final String table,
			final String columns,
			final String header
	) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(file)))
		{
			ps.println("-- " + header);
			if (items.iterator().hasNext())
			{
				ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
				var stream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(items.iterator(), Spliterator.ORDERED), false);
				if (comparator != null)
				{
					stream = stream.sorted(comparator);
				}
				int[] i = {1};
				stream.forEach(e -> {
					if (i[0] != 1)
					{
						ps.print(",\n");
					}
					String values = e.dataRow();
					String comment = e.comment();
					String row = comment != null ? String.format("(%d,%s) /* %s */", i[0], values, comment) : String.format("(%s)", values);
					ps.print(row);
					i[0]++;
				});
				ps.println(";");
			}
		}
	}

	public static <T extends Insertable> void insertFragmented2(
			final Iterable<T> items,
			final Comparator<T> comparator,
			final File file,
			final String table,
			final String columns,
			final String header
	) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(file)))
		{
			ps.println("-- " + header);
			if (items.iterator().hasNext())
			{
				ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
				var stream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(items.iterator(), Spliterator.ORDERED), false);
				if (comparator != null)
				{
					stream = stream.sorted(comparator);
				}
				int[] i = {0, 0};
				stream.forEach(e -> {
					if (i[1] == 100000)
					{
						ps.println(";");
						ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
						i[1] = 0;
					}
					if (i[1] != 0)
					{
						ps.print(",\n");
					}
					String values = e.dataRow();
					String comment = e.comment();
					String row = comment != null ? String.format("(%d,%s) /* %s */", i[0], values, comment) : String.format("(%s)", values);
					ps.print(row);
					i[0]++;
					i[1]++;
				});
				ps.println(";");
			}
		}
	}

	// S T R I N G

	public static void insertStringMap2(
			final Iterable<String> items,
			final Function<String, Integer> resolver,
			final File file,
			final String table,
			final String columns,
			final String header
	) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(file)))
		{
			ps.println("-- " + header);
			if (items.iterator().hasNext())
			{
				ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
				int[] i = {0};
				items.forEach((key) -> {
					if (i[0] != 0)
					{
						ps.print(",\n");
					}
					var id = resolver.apply(key);
					String row = String.format("(%d,'%s')", id, Utils.escape(key));
					ps.print(row);
					i[0]++;
				});
				ps.println(";");
			}
		}
	}

	// G E N E R I C

	public static <K, V> void insert2(
			final Iterable<K> items,
			final Function<K, V> resolver,
			final File file,
			final String table,
			final String columns,
			final String header,
			final BiFunction<K, V, String> stringifier
	) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(file)))
		{
			ps.println("-- " + header);
			if (items.iterator().hasNext())
			{
				ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
				int[] i = {0};
				items.forEach(k -> {
					if (i[0] != 0)
					{
						ps.print(",\n");
					}
					var v = resolver.apply(k);
					String values = stringifier.apply(k, v);
					String row = String.format("(%s)", values);
					ps.print(row);
					i[0]++;
				});
				ps.println(";");
			}
		}
	}

	// R E S O L V A L B L E

	public static <T extends Resolvable<U, R>, U, R> void resolveAndInsert2(
			final Iterable<T> items,
			final Comparator<T> comparator,
			final File file,
			final String table,
			final String columns,
			final String header,
			final Function<U, R> foreignResolver,
			final Function<R, String> stringifier,
			final String... resolvedColumns)
			throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(file)))
		{
			ps.println("-- " + header);
			if (items.iterator().hasNext())
			{
				ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns + "," + String.join(",", resolvedColumns));
				var stream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(items.iterator(), Spliterator.ORDERED), false);
				if (comparator != null)
				{
					stream = stream.sorted(comparator);
				}
				int[] i = {0};
				stream.forEach(e -> {
					if (i[0] != 0)
					{
						ps.print(",\n");
					}
					R resolved = e.resolve(foreignResolver);
					String sqlResolved = stringifier.apply(resolved);
					String values = e.dataRow();
					String comment = e.comment();
					String row = comment != null ? String.format("(%s,%s) /* %s */", values, sqlResolved, comment) : String.format("(%s,%s)", values, sqlResolved);
					ps.print(row);
					i[0]++;
				});
				ps.println(";");
			}
		}
	}

	public static <T extends Resolvable<U, R>, U, R> void resolveAndInsert2(
			final Iterable<T> items,
			final Function<T, Integer> resolver,
			final File file,
			final String table,
			final String columns,
			final String header,
			boolean withNumber,
			final Function<U, R> foreignResolver,
			final Function<R, String> stringifier,
			final String... resolvedColumns
	) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(file)))
		{
			ps.println("-- " + header);
			if (items.iterator().hasNext())
			{
				ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns + "," + String.join(",", resolvedColumns));
				int[] i = {0};
				items.forEach((key) -> {
					if (i[0] != 0)
					{
						ps.print(",\n");
					}
					var id = resolver.apply(key);
					R resolved = key.resolve(foreignResolver);
					String sqlResolved = stringifier.apply(resolved);
					String values = key.dataRow();
					String comment = key.comment();
					String row = withNumber ?
							(comment != null ? String.format("(%d,%s,%s) /* %s */", id, values, sqlResolved, comment) : String.format("(%d,%s,%s)", id, values, sqlResolved)) :
							(comment != null ? String.format("(%s,%s) /* %s */", values, sqlResolved, comment) : String.format("(%s,%s)", values, sqlResolved));
					ps.print(row);
					i[0]++;
				});
				ps.println(";");
			}
		}
	}
}
