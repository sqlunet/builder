package org.sqlbuilder.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.function.Function;

public class Insert
{
	// L I S T

	public static <T extends Insertable> void insert(final List<T> list, final File file, final String table, final String columns, final String header) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(file)))
		{
			ps.println("-- " + header);
			if (list.size() > 0)
			{
				ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
				// private static <T extends Insertable> void insert(final List<T> list, final PrintStream ps)
				{
					int[] i = {0};
					list.forEach(item -> {

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
				}
				ps.println(";");
			}
		}
	}

	// M A P

	public static <T extends Insertable> void insert(final Map<T, Integer> map, final File file, final String table, final String columns, final String header, boolean withNumber) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(file)))
		{
			ps.println("-- " + header);
			if (map.size() > 0)
			{
				ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
				// private static <T extends Insertable> void insert(final Map<T, Integer> map, final PrintStream ps)
				{
					int[] i = {0};
					map.forEach((key, id) -> {

						if (i[0] != 0)
						{
							ps.print(",\n");
						}
						String values = key.dataRow();
						String comment = key.comment();
						String row = withNumber ?
								(comment != null ? String.format("(%d,%s) /* %s */", id, values, comment) : String.format("(%d,%s)", id, values)) :
								(comment != null ? String.format("(%s) /* %s */", values, comment) : String.format("(%s)", values));
						ps.print(row);
						i[0]++;
					});
				}
				ps.println(";");
			}
		}
	}

	public static <T extends Insertable> void insert(final Map<T, Integer> map, final File file, final String table, final String columns, final String header) throws FileNotFoundException
	{
		insert(map, file, table, columns, header, true);
	}

	public static <T extends Resolvable<U, R>, U, R> void resolveAndInsert(final Map<T, Integer> map, final File file, final String table, final String columns, final String header, boolean withNumber,  //
			final Function<U, R> resolver, //
			final Function<R, String> stringifier, //
			final String... resolvedColumns)  //
			throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(file)))
		{
			ps.println("-- " + header);
			if (map.size() > 0)
			{
				ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns + "," + String.join(",", resolvedColumns));

				int[] i = {0};
				map.forEach((key, id) -> {

					if (i[0] != 0)
					{
						ps.print(",\n");
					}
					R resolved = key.resolve(resolver);
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

	// S T R I N G   M A P

	public static void insertStringMap(final Map<String, Integer> map, final File file, final String table, final String columns, final String header) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(file)))
		{
			ps.println("-- " + header);
			if (map.size() > 0)
			{
				ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
				// public static void insertStringMap(final Map<String, Integer> map, final PrintStream ps)
				{
					int[] i = {0};
					map.forEach((key, id) -> {

						if (i[0] != 0)
						{
							ps.print(",\n");
						}
						String row = String.format("(%d,'%s')", id, Utils.escape(key));
						ps.print(row);
						i[0]++;
					});
				}
				ps.println(";");
			}
		}
	}

	// G E N E R I C   M A P

	public static <K, V> void insert(final Map<K, V> map, final File file, final String table, final String columns, final String header, final Function<Map.Entry<K, V>, String> stringifier) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(file)))
		{
			ps.println("-- " + header);
			if (!map.isEmpty())
			{
				ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
				int[] i = {0};
				map.entrySet().forEach(e -> {

					if (i[0] != 0)
					{
						ps.print(",\n");
					}
					String values = stringifier.apply(e);
					String row = String.format("(%s)", values);
					ps.print(row);
					i[0]++;
				});
				ps.println(";");
			}
		}
	}

	// S E T

	public static <T extends Insertable> void insert(final Set<T> set, final Comparator<T> comparator, final File file, final String table, final String columns, final String header) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(file)))
		{
			ps.println("-- " + header);
			if (!set.isEmpty())
			{
				ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
				// private static <T extends Insertable> void insert(final Set<T> set, final Comparator<T> comparator, final PrintStream ps)
				{
					int[] i = {0};
					var stream = set.stream();
					if (comparator != null)
					{
						stream = stream.sorted(comparator);
					}
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
				}
				ps.println(";");
			}
		}
	}

	public static <T extends Insertable> void insertFragmented(final Set<T> set, final Comparator<T> comparator, final File file, final String table, final String columns, final String header) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(file)))
		{
			ps.println("-- " + header);
			if (!set.isEmpty())
			{
				ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
				// private static <T extends Insertable> void insertFragmented(final Set<T> set, final Comparator<T> comparator, final PrintStream ps)
				{
					int[] i = {0, 0};
					var stream = set.stream();
					if (comparator != null)
					{
						stream = stream.sorted(comparator);
					}
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
				}
				ps.println(";");
			}
		}
	}

	public static <T extends Insertable> void insertAndIncrement(final Set<T> set, final Comparator<T> comparator, final File file, final String table, final String columns, final String header) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(file)))
		{
			ps.println("-- " + header);
			if (!set.isEmpty())
			{
				ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
				// private static <T extends Insertable> void insertAndIncrement(final Set<T> set, final Comparator<T> comparator, final PrintStream ps)
				{
					int[] i = {1};
					var stream = set.stream();
					if (comparator != null)
					{
						stream = stream.sorted(comparator);
					}
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
				}
				ps.println(";");
			}
		}
	}

	public static <T extends Resolvable<U, R>, U, R> void resolveAndInsert(final Set<T> set, final Comparator<T> comparator, final File file, final String table, final String columns, final String header, //
			final Function<U, R> resolver, //
			final Function<R, String> stringifier, //
			final String... resolvedColumns) //
			throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(file)))
		{
			ps.println("-- " + header);
			if (!set.isEmpty())
			{
				ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns + "," + String.join(",", resolvedColumns));
				// private static <T extends Insertable> void insert(final Set<T> set, final Comparator<T> comparator, final PrintStream ps)
				{
					int[] i = {0};
					var stream = set.stream();
					if (comparator != null)
					{
						stream = stream.sorted(comparator);
					}
					stream.forEach(e -> {

						if (i[0] != 0)
						{
							ps.print(",\n");
						}
						R resolved = e.resolve(resolver);
						String sqlResolved = stringifier.apply(resolved);
						String values = e.dataRow();
						String comment = e.comment();
						String row = comment != null ? String.format("(%s,%s) /* %s */", values, sqlResolved, comment) : String.format("(%s,%s)", values, sqlResolved);
						ps.print(row);
						i[0]++;
					});
				}
				ps.println(";");
			}
		}
	}
}
