package og.sqlbuilder.sumo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sqlbuilder.su.Dump;
import org.sqlbuilder.su.KBLoader;
import org.sqlbuilder.su.SuProcessor;
import org.sqlbuilder.su.objects.Formula;
import org.sqlbuilder.su.objects.SUFile;
import org.sqlbuilder.su.objects.Term;

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

	@Test
	public void testDumpFunctions()
	{
		Dump.dumpFunctions(KBLoader.kb, TestUtils.OUT);
	}

	@Test
	public void testDumpRelations()
	{
		Dump.dumpFunctions(KBLoader.kb, TestUtils.OUT);
	}

	@BeforeAll
	public static void init()
	{
		KBLoader.kb.buildRelationCaches();

		SuProcessor.collectFiles(KBLoader.kb);
		SuProcessor.collectTerms(KBLoader.kb);
		SuProcessor.collectFormulas(KBLoader.kb);

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
