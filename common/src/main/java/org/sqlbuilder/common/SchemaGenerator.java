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
		ResourceBundle bundle = ResourceBundle.getBundle(compat ? "NamesCompat" : "Names");
		Variables.set(bundle);
		new SchemaGenerator().generate(args);
	}

	/**
	 * Generate schema
	 *
	 * @param args command-line arguments
	 * @throws IOException io exception
	 */
	public void generate(String[] args) throws IOException
	{
		File output = null;

		// Output
		String arg1 = args[0];
		if (!"-".equals(arg1))
		{
			if (arg1.endsWith(".sql"))
			{
				System.err.println("Output to file " + arg1);
				output = new File(arg1);
				if (output.exists())
				{
					System.err.println("Overwrite " + output.getAbsolutePath());
					System.exit(1);
				}
				//noinspection ResultOfMethodCallIgnored
				output.createNewFile();
			}
			else
			{
				output = new File(arg1);
				if (!output.exists())
				{
					// System.err.println("Output to new dir " + arg1);
					//noinspection ResultOfMethodCallIgnored
					output.mkdirs();
				}
			}
		}

		// Input
		String inputSubdir = args[1];
		String[] inputs = Arrays.copyOfRange(args, 2, args.length);

		// Single output if console or file
		if (output == null || output.isFile())
		{
			try (PrintStream ps = output == null ? System.out : new PrintStream(output))
			{
				processTemplates(inputSubdir, inputs, (is, name) -> {

					try
					{
						Variables.varSubstitutionInIS(is, ps, true);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				});
			}
		}

		// Multiple outputs if output is directory
		else if (output.isDirectory())
		{
			final File dir = output;
			processTemplates(inputSubdir, inputs, (is, name) -> {

				System.err.println(name);
				File output2 = new File(dir, name);
				try (PrintStream ps = new PrintStream(output2))
				{
					Variables.varSubstitutionInIS(is, ps, true);
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
	 * @param path     path of inputs
	 * @param inputs   inputs
	 * @param consumer string consumer
	 * @throws IOException io exception
	 */
	private void processTemplates(final String path, final String[] inputs, final BiConsumer<InputStream, String> consumer) throws IOException
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
			String prefix = "sqltemplates/" + path + "/";
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
			final URL url = SchemaGenerator.class.getResource("/sqltemplates/" + path);
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
}
