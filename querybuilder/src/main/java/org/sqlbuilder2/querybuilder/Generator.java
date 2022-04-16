/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package org.sqlbuilder2.querybuilder;

import org.sqlbuilder.common.Variables;

import java.io.*;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Generator
 *
 * @author <a href="mailto:1313ou@gmail.com">Bernard Bou</a>
 */
public class Generator
{
	// P R O P E R T I E S

	private static void doGenerateQProperties(final Supplier<String[]> keys, final Function<String, String[]> q, final Variables variables, final PrintStream ps)
	{
		doGenerateProperties(keys, q, r -> variables.varSubstitution(r, false), ps);
	}

	private static void doGenerateQVProperties(final Supplier<String[]> keys, final Function<String, String[]> q, final PrintStream ps)
	{
		doGenerateProperties(keys, q, Function.identity(), ps);
	}

	private static void doGenerateProperties(final Supplier<String[]> keys, final Function<String, String[]> q, final Function<String, String> transformer, final PrintStream ps)
	{
		for (String key : keys.get())
		{
			String[] result = q.apply(key);
			ps.println("# " + key);
			String keyLow = key.toLowerCase(Locale.ROOT);
			ps.println(keyLow + ".t=" + nullable(transformer.apply(result[0])));
			if (result[1] != null)
			{
				ps.println(keyLow + ".p=" + nullable(transformer.apply(result[1])));
			}
			if (result[2] != null)
			{
				ps.println(keyLow + ".s=" + nullable(transformer.apply(result[2])));
			}
			if (result[3] != null)
			{
				ps.println(keyLow + ".a=" + nullable(transformer.apply(result[3])));
			}
			if (result[4] != null)
			{
				ps.println(keyLow + ".g=" + nullable(transformer.apply(result[4])));
			}
			if (result.length > 5 && result[5] != null)
			{
				ps.println(keyLow + ".o=" + nullable(transformer.apply(result[5])));
			}
			ps.println();
		}
	}

	private static String nullable(String s)
	{
		return s == null ? "" : s;
	}

	// Q C L A S S

	private static void doGenerateQVClass(final Supplier<String[]> keys, final Function<String, String[]> q, final String className, final String packageName, final PrintStream ps)
	{
		doGenerateClass(keys, q, className, packageName, Function.identity(), ps);
	}

	private static void doGenerateQClass(final Supplier<String[]> keys, final Function<String, String[]> q, final Variables variables, final String className, final String packageName, final PrintStream ps)
	{
		doGenerateClass(keys, q, className, packageName, r -> variables.varSubstitution(r, false), ps);
	}

	private static void doGenerateClass(final Supplier<String[]> keys, final Function<String, String[]> q, final String className, final String packageName, final Function<String, String> transformer, final PrintStream ps)
	{
		ps.println("package " + packageName + ";\n");
		ps.println("public class " + className + "\n{\n");
		for (String key : keys.get())
		{
			ps.printf("\tstatic public class %s%n\t{%n", key);
			String[] result = q.apply(key);
			assert result != null : key;

			if (result[0] != null)
			{
				ps.printf("\t\tstatic public final String TABLE = %s;%n", transformer.apply(result[0]));
			}
			if (result[1] != null)
			{
				ps.printf("\t\tstatic public final String[] PROJECTION = %s;%n", transformer.apply(result[1]));
			}
			if (result[2] != null)
			{
				ps.printf("\t\tstatic public final String SELECTION = %s;%n", transformer.apply(result[2]));
			}
			if (result[3] != null)
			{
				ps.printf("\t\tstatic public final String[] ARGS = %s;%n", transformer.apply(result[3]));
			}
			if (result[4] != null)
			{
				ps.printf("\t\tstatic public final String GROUPBY = %s;%n", transformer.apply(result[4]));
			}
			if (result.length > 5 && result[5] != null)
			{
				ps.printf("\t\tstatic public final String ORDERBY = %s;%n", transformer.apply(result[5]));
			}
			ps.println("\t}");
			ps.println();
		}
		ps.println("}");
	}

	// V C L A S S

	private static void doGenerateVClass(final Variables variables, final String className, final String packageName, final PrintStream ps)
	{
		ps.println("package " + packageName + ";\n");
		ps.println("public class " + className + " {\n");
		variables.export(ps);
		ps.println("}");
	}

	// I N S T A N T I A T E

	private static void doInstantiate(final File source, final Variables variables, final PrintStream ps) throws IOException
	{
		System.out.println("source:  " + source);
		variables.varSubstitutionInFile(source, ps, false, false, true);
	}

	// V A R I A B L E S

	private static Variables makeVariablesFromProperties(final String... propertiesPaths) throws IOException
	{
		return Variables.make(propertiesPaths);
	}

	// M A I N

	public static void generateQVClass(final String factoryPath, final String dest, final String className, final String packageName) throws Exception
	{
		var factory = Runner.build(factoryPath);
		try (PrintStream ps = makePs(dest, className + ".java"))
		{
			doGenerateQVClass(factory.first, factory.second, className, packageName, ps);
		}
	}

	public static void generateQClass(final String factoryPath, final String dest, final String className, final String packageName, final String[] namesPaths) throws Exception
	{
		var factory = Runner.build(factoryPath);
		var variables = makeVariablesFromProperties(namesPaths);
		try (PrintStream ps = makePs(dest, className + ".java"))
		{
			doGenerateQClass(factory.first, factory.second, variables, className, packageName, ps);
		}
	}

	public static void generateVClass(final String dest, final String className, final String packageName, final String[] namesPaths) throws Exception
	{
		var variables = makeVariablesFromProperties(namesPaths);
		try (PrintStream ps = makePs(dest, className + ".java"))
		{
			doGenerateVClass(variables, className, packageName, ps);
		}
	}

	public static void generateQVProperties(final String factoryPath, final String outDir, final String fileName) throws Exception
	{
		var factory = Runner.build(factoryPath);
		try (PrintStream ps = makePs(outDir, fileName + ".properties"))
		{
			doGenerateQVProperties(factory.first, factory.second, ps);
		}
	}

	public static void generateQProperties(final String factoryPath, final String dest, final String fileName, final String[] namesPaths) throws Exception
	{
		var factory = Runner.build(factoryPath);
		var variables = makeVariablesFromProperties(namesPaths);
		try (PrintStream ps = makePs(dest, fileName + ".properties"))
		{
			doGenerateQProperties(factory.first, factory.second, variables, ps);
		}
	}

	public static void instantiate(final String[] sourcePaths, final String dest, final String[] namesPaths) throws Exception
	{
		var variables = makeVariablesFromProperties(namesPaths);
		for (var sourcePath : sourcePaths)
		{
			var source = new File(sourcePath);
			var fileName = source.getName();
			try (PrintStream ps = makePs(dest, fileName))
			{
				doInstantiate(source, variables, ps);
			}
		}
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	static PrintStream makePs(final String dest, final String fileName) throws FileNotFoundException
	{
		boolean isConsole = "-".equals(dest);
		if (isConsole)
		{
			return System.out;
		}
		File dir = new File(dest);
		if (!dir.exists())
		{
			dir.mkdirs();
		}
		return new PrintStream(new FileOutputStream(new File(dir, fileName)));
	}

	// F R O M   O P

	private static boolean generateOps(final String op, final String inDir, final String outDir, final String className, final String packageName) throws Exception
	{
		String factoryPath = new File(inDir, "Factory.java").getAbsolutePath();
		String[] namesPaths = {new File(inDir, "Names.properties").getAbsolutePath(), new File(inDir, "NamesExtra.properties").getAbsolutePath()};
		switch (op)
		{
			case "generate_qv_class":
			{
				generateQVClass(factoryPath, outDir, className, packageName);
				return true;
			}

			case "generate_q_class":
			{
				generateQClass(factoryPath, outDir, className, packageName, namesPaths);
				return true;
			}

			case "generate_v_class":
			{
				generateVClass(outDir, className, packageName, namesPaths);
				return true;
			}

			case "generate_qv_properties":
			{
				generateQVProperties(factoryPath, outDir, className);
				return true;
			}

			case "generate_q_properties":
			{
				generateQProperties(factoryPath, outDir, className, namesPaths);
				return true;
			}
		}
		return false;
	}

	private static boolean instantiateOps(final String op, final String inDir, final String source, final String outDir, final String dest) throws Exception
	{
		String[] namesPaths = {new File(inDir, "Names.properties").getAbsolutePath(), new File(inDir, "NamesExtra.properties").getAbsolutePath()};
		switch (op)
		{
			case "instantiate":
			{
				String[] sources = {new File(inDir, source).getAbsolutePath()};
				instantiate(sources, outDir, namesPaths);
				return true;
			}

			case "list_variables":
			{
				try (PrintStream ps = "-".equals(outDir) ? System.out : new PrintStream(new FileOutputStream(new File(outDir, dest))))
				{
					File f = new File(inDir, source);
					ps.println("Variables used in " + f + ":");
					Variables.dumpVars(f, ps::println);
				}
				return true;
			}
		}
		return false;
	}

	private static boolean exportOps(final String op, final String inDir, final String outDir, final String dest) throws Exception
	{
		String namesPath = new File(inDir, "Names.properties").getAbsolutePath();
		String namesExtraPath = new File(inDir, "NamesExtra.properties").getAbsolutePath();
		if ("export".equals(op))
		{
			var variables = makeVariablesFromProperties(namesPath, namesExtraPath);
			try (PrintStream ps = "-".equals(outDir) ? System.out : new PrintStream(new FileOutputStream(new File(outDir, dest))))
			{
				variables.export(ps);
			}
			return true;
		}
		return false;
	}

	public static void main(final String[] args) throws Exception
	{
		// op, inDir, outDir, className, packageName
		if (args.length < 5 || !generateOps(args[0], args[1], args[2], args[3], args[4]))
		{
			// op, inDir, source, outDir, dest
			if (args.length < 4 || !instantiateOps(args[0], args[1], args[2], args[3], "-".equals(args[3]) ? null : args[4]))
			{
				// op, inDir, outDir, dest
				if (args.length < 3 || !exportOps(args[0], args[1], args[2], "-".equals(args[2]) ? null : args[3]))
				{
					System.err.println("Invalid op " + args[0]);
				}
			}
		}
	}
}
