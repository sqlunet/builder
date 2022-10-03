package org.sqlbuilder.sumo;

import org.sqlbuilder.common.AlreadyFoundException;
import org.sqlbuilder.common.SetCollector;
import org.sqlbuilder.sumo.joins.Term_Sense;
import org.sqlbuilder.sumo.objects.Term;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WnProcessor
{
	private static final String[] POSES = {"noun", "verb", "adj", "adv"};

	private static final String SUMO_TEMPLATE = "WordNetMappings/WordNetMappings30-%s.txt";

	private final String home;

	public WnProcessor(final String home)
	{
		this.home = home;
	}

	public void run(final PrintStream ps, final PrintStream pse) throws IOException
	{
		for (final String pos : POSES)
		{
			collect(pos, pse);
		}
		try (SetCollector<Term> ignored = Term.COLLECTOR.open())
		{
			for (final Term_Sense map : Term_Sense.SET)
			{
				String row = map.dataRow();
				String comment = map.comment();
				ps.printf("%s -- %s%n", row, comment);
			}
		}
	}

	public void collect(final String posName, final PrintStream pse) throws IOException
	{
		final String filename = this.home + File.separator + String.format(SUMO_TEMPLATE, posName);

		// pos
		final char pos = posName.charAt(0);

		// iterate on synsets
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(filename)))))
		{
			int lineno = 0;
			String line;
			while ((line = reader.readLine()) != null)
			{
				lineno++;
				line = line.trim();
				if (line.isEmpty() || line.charAt(0) == ' ' || line.charAt(0) == ';' || !line.contains("&%"))
				{
					continue;
				}

				// read
				try
				{
					final String term = Term.parse(line);
					/* final SUMOTerm_Sense mapping = */
					Term_Sense.parse(term, line, pos); // side effect: term mapping collected into set
				}
				catch (IllegalArgumentException iae)
				{
					pse.println("line " + lineno + '-' + pos + " " + ": ILLEGAL [" + iae.getMessage() + "] : " + line);
				}
				catch (AlreadyFoundException afe)
				{
					pse.println("line " + lineno + '-' + pos + " " + ": DUPLICATE [" + afe.getMessage() + "] : " + line);
				}
			}
		}
	}
}
