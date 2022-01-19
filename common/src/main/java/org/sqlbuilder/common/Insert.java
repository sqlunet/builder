package org.sqlbuilder.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

public class Insert
{
	public static <T extends Insertable<T>> void insert(final Map<T, Integer> map, final File file, String table, String columns) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(file)))
		{
			ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
			insert(map, ps);
			ps.println(";");
		}
	}

	public static <T extends Insertable<T>> void insert(final Map<T, Integer> map, final PrintStream ps)
	{
		int[] i = {0};
		map.forEach((key, value) -> {

			if (i[0] != 0)
			{
				ps.print(",\n");
			}
			String values = key.dataRow();
			String row = String.format("(%d,%s)", value, values);
			ps.print(row);
			i[0]++;
		});
	}

	public static <T extends Insertable<T>> void insert(final Set<T> set, final Comparator<T> comparator, final File file, final String table, final String columns) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(file)))
		{
			ps.printf("INSERT INTO %s (%s) VALUES%n", table, columns);
			insert(set, comparator, ps);
			ps.println(";");
		}
	}

	public static <T extends Insertable<T>> void insert(final Set<T> set, final Comparator<T> comparator, final PrintStream ps)
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
			String row = String.format("(%s)", values);
			ps.print(row);
			i[0]++;
		});
	}
}
