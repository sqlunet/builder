package org.sqlbuilder.common;

import org.sqlunet.wn.C;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Locale;

public class GenerateV
{
	private static void generateNamesWithNamesAsValues(PrintStream ps, Class<?> clazz, String module)
	{
		ps.println(String.format("package org.sqlunet.%s;", module));
		ps.println("public class V {");

		Arrays.stream(clazz.getDeclaredClasses()).forEach(c -> {
			ps.printf("static public class %s {%n", c.getSimpleName());
			Arrays.stream(c.getDeclaredFields()) //
					.filter(f -> !"CONTENT_URI_TABLE".equals(f.getName())) //
					.forEach(f -> {
						ps.printf("static public final String %s = \"${%s.%s}\";%n", f.getName(), c.getSimpleName().toLowerCase(Locale.ROOT), f.getName().toLowerCase(Locale.ROOT));
					});
			ps.println("}");
		});
		ps.println("}");
	}

	public static void main(final String[] args) throws FileNotFoundException, ClassNotFoundException
	{
		var module = args[0];
		var clazz = Class.forName(args[1]);
		try (PrintStream ps = new PrintStream(new FileOutputStream(args[2])))
		{
			generateNamesWithNamesAsValues(ps, clazz, module);
		}
	}
}
