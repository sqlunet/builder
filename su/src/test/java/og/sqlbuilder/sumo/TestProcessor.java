package og.sqlbuilder.sumo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sqlbuilder.common.NotFoundException;
import org.sqlbuilder.sumo.KBLoader;
import org.sqlbuilder.sumo.SumoProcessor;
import org.sqlbuilder.sumo.objects.Formula;
import org.sqlbuilder.sumo.objects.SUFile;
import org.sqlbuilder.sumo.objects.Term;

import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith({KbLoaderExtension.class})
public class TestProcessor
{
	@Test
	public void testProcessFiles()
	{
		try // (SetCollector<SUMOFile> ignored = SUMOFile.COLLECTOR.open())
		{
			SumoProcessor.insertFiles(TestUtils.OUT, SUFile.COLLECTOR.keySet(), "files", "fileid,file,version,date");
		}
		catch (Exception e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testProcessTermsAndAttrs() throws NotFoundException
	{
		try // (SetCollector<SUMOTerm> ignored = SUMOTerm.COLLECTOR.open())
		{
			SumoProcessor.insertTermsAndAttrs(TestUtils.OUT, TestUtils.OUT, Term.COLLECTOR.keySet(), KBLoader.kb, "terms", "sumoid,term", "terms_attr", "sumoid,attr");
		}
		catch (Exception e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testProcessTermAttrs() throws NotFoundException
	{
		try // (SetCollector<SUMOTerm> ignored = SUMOTerm.COLLECTOR.open())
		{
			SumoProcessor.insertTermAttrs(TestUtils.OUT, Term.COLLECTOR.keySet(), KBLoader.kb, "terms_attr", "sumoid,attr");
		}
		catch (Exception e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testProcessTerms() throws NotFoundException
	{
		try // (SetCollector<SUMOTerm> ignored = SUMOTerm.COLLECTOR.open())
		{
			SumoProcessor.insertTerms(TestUtils.OUT, TestUtils.OUT, Term.COLLECTOR.keySet(), "terms", "sumoid,term");
		}
		catch (Exception e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testProcessFormulasAndArgs() throws NotFoundException
	{
		try //(
		//SetCollector<SUMOTerm> ignored = SUMOTerm.COLLECTOR.open(); //
		//SetCollector<SUMOFile> ignored2 = SUMOFile.COLLECTOR.open(); //
		//SetCollector<SUMOFormula> ignored3 = SUMOFormula.COLLECTOR.open(); //
		//)
		{
			SumoProcessor.insertFormulasAndArgs(TestUtils.OUT, TestUtils.OUT, Formula.COLLECTOR.keySet(), "formulas", "formulaid,formula,fileid", "formulas_args", "formulaid,sumoid,parsetype,argnum");
		}
		catch (Exception e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testProcessFormulas() throws NotFoundException
	{
		try //(
		//SetCollector<SUMOTerm> ignored = SUMOTerm.COLLECTOR.open(); //
		//SetCollector<SUMOFile> ignored2 = SUMOFile.COLLECTOR.open(); //
		//SetCollector<SUMOFormula> ignored3 = SUMOFormula.COLLECTOR.open(); //
		//)
		{
			SumoProcessor.insertFormulas(TestUtils.OUT, Formula.COLLECTOR.keySet(), "formulas", "formulaid,formula,fileid");
		}
		catch (Exception e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testProcessFormulasArgs() throws NotFoundException
	{
		try //(
		//SetCollector<SUMOTerm> ignored = SUMOTerm.COLLECTOR.open(); //
		//SetCollector<SUMOFile> ignored2 = SUMOFile.COLLECTOR.open(); //
		//SetCollector<SUMOFormula> ignored3 = SUMOFormula.COLLECTOR.open(); //
		//)
		{
			SumoProcessor.insertFormulaArgs(TestUtils.OUT, Formula.COLLECTOR.keySet(), "formulas_args", "formulaid,sumoid,parsetype,argnum");
		}
		catch (Exception e)
		{
			fail(e.getMessage());
		}
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

	public static void main(String[] args) throws NotFoundException
	{
		new KBLoader().load();
		init();
		TestProcessor p = new TestProcessor();
		p.testProcessFiles();
		p.testProcessTermsAndAttrs();
		p.testProcessFormulasAndArgs();
		shutdown();
	}
}
