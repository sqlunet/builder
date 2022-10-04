package og.sqlbuilder.sumo;

import org.sqlbuilder.sumo.Kb;
import org.sqlbuilder.sumo.SumoModule;

import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUtils
{
	public static final PrintStream NULL_OUT = new PrintStream(new OutputStream()
	{
		public void write(int b)
		{
			//DO NOTHING
		}
	});

	public static PrintStream OUT = System.out;

	public static final PrintStream OUT_INFO = System.out;

	public static PrintStream OUT_WARN = System.out;

	public static PrintStream OUT_ERR = System.err;

	public static void turnOffLogging()
	{
		SumoModule.turnOffLogging();

		boolean silent = System.getProperties().containsKey("SILENT");
		if (silent)
		{
			OUT = NULL_OUT;
			OUT_WARN = NULL_OUT;
		}
	}

	public static void getRelValences(final String[] rels, final Kb kb)
	{
		System.out.println();
		for (String rel : rels)
		{
			System.out.printf("'%s' valence %s%n", rel, kb.getValence(rel));
		}
	}
}
