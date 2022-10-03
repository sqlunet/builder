package og.sqlbuilder.sumo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sqlbuilder.sumo.Dump;
import org.sqlbuilder.sumo.KBLoader;
import org.sqlbuilder.sumo.SumoProcessor;
import org.sqlbuilder.sumo.objects.Formula;
import org.sqlbuilder.sumo.objects.SUFile;
import org.sqlbuilder.sumo.objects.Term;

@ExtendWith({KbLoaderExtension.class})
public class TestDump
{
	@Test
	public void testDumpTerms()
	{
		Dump.dumpTerms(KBLoader.kb, TestUtils.OUT);
	}

	@Test
	public void testDumpFormulas()
	{
		Dump.dumpFormulas(KBLoader.kb, TestUtils.OUT);
	}

	@Test
	public void testDumpPredicates()
	{
		Dump.dumpPredicates(KBLoader.kb, TestUtils.OUT);
	}

	@BeforeAll
	public static void init()
	{
		SumoProcessor.collectFiles(KBLoader.kb);
		SumoProcessor.collectTerms(KBLoader.kb);
		SumoProcessor.collectFormulas(KBLoader.kb);

		SUFile.COLLECTOR.open();
		Term.COLLECTOR.open();
		Formula.COLLECTOR.open();
	}

	@AfterAll
	public static void shutdown()
	{
		SUFile.COLLECTOR.close();
		Term.COLLECTOR.close();
		Formula.COLLECTOR.close();
	}

	public static void main(String[] args)
	{
		new KBLoader().load();
		init();
		TestDump d = new TestDump();
		d.testDumpTerms();
		d.testDumpFormulas();
		d.testDumpFormulas();
	}
}
