package org.sqlbuilder.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Insert
{
	// L I S T

	public static <T extends Insertable> void insert(final List<T> list, final File file, String table, String columns) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(file)))
		{
			if (list.size() > 0)
			{
				ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
				insert(list, ps);
				ps.println(";");
			}
		}
	}

	private static <T extends Insertable> void insert(final List<T> list, final PrintStream ps)
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

	// M A P

	public static <T extends Insertable> void insert(final Map<T, Integer> map, final File file, String table, String columns) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(file)))
		{
			if (map.size() > 0)
			{
				ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
				insert(map, ps);
				ps.println(";");
			}
		}
	}

	private static <T extends Insertable> void insert(final Map<T, Integer> map, final PrintStream ps)
	{
		int[] i = {0};
		map.forEach((key, value) -> {

			if (i[0] != 0)
			{
				ps.print(",\n");
			}
			String values = key.dataRow();
			String comment = key.comment();
			String row = comment != null ? String.format("(%d,%s) /* %s */", value, values, comment) : String.format("(%d,%s)", value, values);
			ps.print(row);
			i[0]++;
		});
	}

	public static <T extends Insertable> void insertNoNumber(final Map<T, Integer> map, final File file, String table, String columns) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(file)))
		{
			if (map.size() > 0)
			{
				ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
				insertNoNumber(map, ps);
				ps.println(";");
			}
		}
	}

	private static <T extends Insertable> void insertNoNumber(final Map<T, Integer> map, final PrintStream ps)
	{
		int[] i = {0};
		map.forEach((key, value) -> {

			if (i[0] != 0)
			{
				ps.print(",\n");
			}
			String values = key.dataRow();
			String comment = key.comment();
			String row = comment != null ? String.format("(%s) /* %s */", values, comment) : String.format("(%s)", values);
			ps.print(row);
			i[0]++;
		});
	}

	// S T R I N G   M A P

	public static void insertStringMap(final Map<String, Integer> map, final File file, String table, String columns) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(file)))
		{
			if (map.size() > 0)
			{
				ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
				insertStringMap(map, ps);
				ps.println(";");
			}
		}
	}

	public static void insertStringMap(final Map<String, Integer> map, final PrintStream ps)
	{
		int[] i = {0};
		map.forEach((key, value) -> {

			if (i[0] != 0)
			{
				ps.print(",\n");
			}
			String row = String.format("(%d,'%s')", value, Utils.escape(key));
			ps.print(row);
			i[0]++;
		});
	}

	// S E T

	public static <T extends Insertable> void insert(final Set<T> set, final Comparator<T> comparator, final File file, final String table, final String columns) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(file)))
		{
			if (set.size() > 0)
			{
				ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
				insert(set, comparator, ps);
				ps.println(";");
			}
		}
	}

	private static <T extends Insertable> void insert(final Set<T> set, final Comparator<T> comparator, final PrintStream ps)
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

	public static <T extends Insertable> void insertFragmented(final Set<T> set, final Comparator<T> comparator, final File file, final String table, final String columns) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(file)))
		{
			if (set.size() > 0)
			{
				ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
				int[] i = {0,0};
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
				ps.println(";");
			}
		}
	}

	public static <T extends Insertable> void insertAndIncrement(final Set<T> set, final Comparator<T> comparator, final File file, final String table, final String columns) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(file)))
		{
			if (set.size() > 0)
			{
				ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
				insertAndIncrement(set, comparator, ps);
				ps.println(";");
			}
		}
	}

	private static <T extends Insertable> void insertAndIncrement(final Set<T> set, final Comparator<T> comparator, final PrintStream ps)
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
}
