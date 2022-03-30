/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package org.sqlunet.plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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

	public static void generateQProperties(final Supplier<String[]> keys, final Function<String, String[]> q, final Variables variables, final PrintStream ps)
	{
		generateProperties(keys, q, r -> variables.varSubstitution(r, false), ps);
	}

	public static void generateQVProperties(final Supplier<String[]> keys, final Function<String, String[]> q, final PrintStream ps)
	{
		generateProperties(keys, q, Function.identity(), ps);
	}

	public static void generateProperties(final Supplier<String[]> keys, final Function<String, String[]> q, final Function<String, String> transformer, final PrintStream ps)
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

	// Q C L A S S

	public static void generateQVClass(final Supplier<String[]> keys, final Function<String, String[]> q, final String className, final String packageName, final PrintStream ps)
	{
		generateClass(keys, q, className, packageName, Function.identity(), ps);
	}

	public static void generateQClass(final Supplier<String[]> keys, final Function<String, String[]> q, final Variables variables, final String className, final String packageName, final PrintStream ps)
	{
		generateClass(keys, q, className, packageName, r -> variables.varSubstitution(r, false), ps);
	}

	public static void generateClass(final Supplier<String[]> keys, final Function<String, String[]> q, final String className, final String packageName, final Function<String, String> transformer, final PrintStream ps)
	{
		ps.println("package " + packageName + ";\n");
		ps.println("public class " + className + "\n{\n");
		for (String key : keys.get())
		{
			ps.printf("\tstatic public class %s%n\t{%n", key);
			String[] result = q.apply(key);
			assert result != null : key;

			ps.printf("\t\tstatic public final String TABLE = %s;%n", transformer.apply(result[0]));
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

	public static void generateVClass(final Variables variables, final String className, final String packageName, final PrintStream ps)
	{
		ps.println("package " + packageName + ";\n");
		ps.println("public class " + className + " {\n");
		variables.export(ps);
		ps.println("}");
	}

	// V A R I A B L E S

	public static Variables makeVariablesFromProperties(final String... propertiesPaths) throws IOException
	{
		return Variables.make(propertiesPaths);
	}

	/*
	public static Variables makeVariablesFromBundle(final String module)
	{
		ResourceBundle bundle = ResourceBundle.getBundle(module + "/" + "Names");
		return Variables.make(bundle);
	}
	*/

	// M A I N

	private static boolean generate(final String op, final String inDir, final String outDir, final String className, final String packageName) throws Exception
	{
		String factoryPath = new File(inDir, "Factory.java").getAbsolutePath();
		String namesPath = new File(inDir, "Names.properties").getAbsolutePath();
		String namesExtraPath = new File(inDir, "NamesExtra.properties").getAbsolutePath();
		switch (op)
		{
			case "generate_qv_class":
			{
				var factory = Runner.build(factoryPath);
				try (PrintStream ps = "-".equals(outDir) ? System.out : new PrintStream(new FileOutputStream(new File(outDir, className + ".java"))))
				{
					generateQVClass(factory.first, factory.second, className, packageName, ps);
				}
				return true;
			}

			case "generate_q_class":
			{
				var factory = Runner.build(factoryPath);
				var variables = makeVariablesFromProperties(namesPath, namesExtraPath);
				try (PrintStream ps = "-".equals(outDir) ? System.out : new PrintStream(new FileOutputStream(new File(outDir, className + ".java"))))
				{
					generateQClass(factory.first, factory.second, variables, className, packageName, ps);
				}
				return true;
			}

			case "generate_qv_properties":
			{
				var factory = Runner.build(factoryPath);
				try (PrintStream ps = "-".equals(outDir) ? System.out : new PrintStream(new FileOutputStream(new File(outDir, className + ".java"))))
				{
					generateQVProperties(factory.first, factory.second, ps);
				}
				return true;
			}

			case "generate_q_properties":
			{
				var factory = Runner.build(factoryPath);
				var variables = makeVariablesFromProperties(namesPath, namesExtraPath);
				try (PrintStream ps = "-".equals(outDir) ? System.out : new PrintStream(new FileOutputStream(new File(outDir, className + ".java"))))
				{
					generateQProperties(factory.first, factory.second, variables, ps);
				}
				return true;
			}

			case "generate_v_class":
			{
				var variables = makeVariablesFromProperties(namesPath, namesExtraPath);
				try (PrintStream ps = "-".equals(outDir) ? System.out : new PrintStream(new FileOutputStream(new File(outDir, className + ".java"))))
				{
					generateVClass(variables, className, packageName, ps);
				}
				return true;
			}
		}
		return false;
	}

	private static boolean instantiate(final String op, final String inDir, final String source, final String outDir, final String dest) throws Exception
	{
		String namesPath = new File(inDir, "Names.properties").getAbsolutePath();
		String namesExtraPath = new File(inDir, "NamesExtra.properties").getAbsolutePath();
		switch (op)
		{
			case "instantiate":
			{
				var variables = makeVariablesFromProperties(namesPath, namesExtraPath);
				try (PrintStream ps = "-".equals(outDir) ? System.out : new PrintStream(new FileOutputStream(new File(outDir, dest))))
				{
					variables.varSubstitutionInFile(new File(inDir, source), ps, false, false);
				}
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

	private static boolean export(final String op, final String inDir, final String outDir, final String dest) throws Exception
	{
		String namesPath = new File(inDir, "Names.properties").getAbsolutePath();
		String namesExtraPath = new File(inDir, "NamesExtra.properties").getAbsolutePath();
		switch (op)
		{
			case "export":
			{
				var variables = makeVariablesFromProperties(namesPath, namesExtraPath);
				try (PrintStream ps = "-".equals(outDir) ? System.out : new PrintStream(new FileOutputStream(new File(outDir, dest))))
				{
					variables.export(ps);
				}
				return true;
			}
		}
		return false;
	}

	private static String nullable(String s)
	{
		return s == null ? "" : s;
	}

	public static void main(final String[] args) throws Exception
	{
		// op, inDir, outDir, className, packageName
		if (args.length < 5 || !generate(args[0], args[1], args[2], args[3], args[4]))
		{
			// op, inDir, source, outDir, dest
			if (args.length < 4 || !instantiate(args[0], args[1], args[2], args[3], "-".equals(args[3]) ? null : args[4]))
			{
				// op, inDir, outDir, dest
				if (args.length < 3 || !export(args[0], args[1], args[2], "-".equals(args[2]) ? null : args[3]))
				{
					System.err.println("Invalid op " + args[0]);
				}
			}
		}
	}
}
