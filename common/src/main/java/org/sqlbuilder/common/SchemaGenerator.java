/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.sqlbuilder.common;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Main class that generates the SQL schema by instantiating templates
 *
 * @author Bernard Bou
 * @see "https://sqlunet.sourceforge.net/schema.html"
 */
public class SchemaGenerator
{
	private Variables variables;

	public SchemaGenerator(final Variables variables)
	{
		this.variables = variables;
	}

	/**
	 * Generate schema
	 *
	 * @param module      module
	 * @param output      output
	 * @param inputSubdir input subdir (from sqltemplates/)
	 * @param inputs      input files (or null if all)
	 * @throws IOException io exception
	 */
	public void generate(final String module, final String output, final String inputSubdir, final String[] inputs) throws IOException
	{
		File outputFileOrDir = null;

		// Output
		if (!"-".equals(output))
		{
			// output is not console
			if (output.endsWith(".sql"))
			{
				// output is plain sql file
				System.err.println("Output to file " + output);
				outputFileOrDir = new File(output);
				if (outputFileOrDir.exists())
				{
					System.err.println("Overwrite " + outputFileOrDir.getAbsolutePath());
					System.exit(1);
				}
				//noinspection ResultOfMethodCallIgnored
				outputFileOrDir.createNewFile();
			}
			else
			{
				// multiple outputs as per inputs
				outputFileOrDir = new File(output);
				if (!outputFileOrDir.exists())
				{
					// System.err.println("Output to new dir " + arg1);
					//noinspection ResultOfMethodCallIgnored
					outputFileOrDir.mkdirs();
				}
			}
		}

		// Input
		// Single output if console or file
		if (outputFileOrDir == null || outputFileOrDir.isFile())
		{
			try (PrintStream ps = outputFileOrDir == null ? System.out : new PrintStream(outputFileOrDir))
			{
				processTemplates(module, inputSubdir, inputs, (is, name) -> {

					try
					{
						variables.varSubstitutionInIS(is, ps, true, true);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				});
			}
		}

		// Multiple outputs if output is directory
		else if (outputFileOrDir.isDirectory())
		{
			final File dir = outputFileOrDir;
			processTemplates(module, inputSubdir, inputs, (is, name) -> {

				System.err.println(name);
				File output2 = new File(dir, name);
				try (PrintStream ps = new PrintStream(output2))
				{
					variables.varSubstitutionInIS(is, ps, true, true);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			});
		}
		else
		{
			System.err.println("Internal error");
		}
	}

	/**
	 * Process input template files
	 *
	 * @param module   module
	 * @param path     path of inputs
	 * @param inputs   inputs
	 * @param consumer string consumer
	 * @throws IOException io exception
	 */
	private void processTemplates(final String module, final String path, final String[] inputs, final BiConsumer<InputStream, String> consumer) throws IOException
	{
		// external resources
		if (inputs != null && inputs.length > 0)
		{
			for (String input : inputs)
			{
				File file = new File(path, input);
				String fileName = Paths.get(input).getFileName().toString();
				try (FileInputStream fis = new FileInputStream(file))
				{
					consumer.accept(fis, fileName);
				}
			}
			return;
		}

		// internal resources
		final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
		if (jarFile.isFile())
		{
			// Run with JAR file
			String prefix = module + "/sqltemplates/" + path + "/";
			try (final JarFile jar = new JarFile(jarFile))
			{
				final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
				while (entries.hasMoreElements())
				{
					final JarEntry entry = entries.nextElement();
					if (entry.isDirectory())
					{
						continue;
					}

					final String name = entry.getName();
					//filter according to the path
					if (name.startsWith(prefix))
					{
						String fileName = Paths.get(name).getFileName().toString();
						try (InputStream is = jar.getInputStream(entry))
						{
							consumer.accept(is, fileName);
						}
					}
				}
			}
		}
		else
		{
			// Run with IDE
			final URL url = SchemaGenerator.class.getResource("/" + module + "/sqltemplates/" + path);
			if (url != null)
			{
				try
				{
					final File dir = new File(url.toURI());
					File[] files = dir.listFiles();
					if (files != null)
					{
						for (File file : files)
						{
							try (FileInputStream fis = new FileInputStream(file))
							{
								consumer.accept(fis, file.getName());
							}
						}
					}
				}
				catch (URISyntaxException ex)
				{
					// never happens
				}
			}
		}
	}

	/**
	 * Main entry point
	 *
	 * @param args command-line arguments
	 * @throws IOException io exception
	 */
	public static void main(String[] args) throws IOException
	{
		boolean compat = false;
		if ("-compat".equals(args[0]))
		{
			compat = true;
			args = Arrays.copyOfRange(args, 1, args.length);
		}

		String module = args[0];
		String output = args[1];
		String inputSubdir = args[2];
		String[] inputs = Arrays.copyOfRange(args, 3, args.length);

		ResourceBundle bundle = ResourceBundle.getBundle(module + "/" + (compat ? "NamesCompat" : "Names"));
		var variables = Variables.make(bundle);
		new SchemaGenerator(variables).generate(module, output, inputSubdir, inputs);
	}
}
