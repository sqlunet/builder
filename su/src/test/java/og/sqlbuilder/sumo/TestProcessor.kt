package og.sqlbuilder.sumo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sqlbuilder.su.KBLoader;
import org.sqlbuilder.su.SuProcessor;
import org.sqlbuilder.su.objects.Formula;
import org.sqlbuilder.su.objects.SUFile;
import org.sqlbuilder.su.objects.Term;

import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith({KbLoaderExtension.class})
public class TestProcessor
{
	@Test
	public void testProcessFiles()
	{
		try // (SetCollector<SUMOFile> ignored = SUMOFile.COLLECTOR.open())
		{
			SuProcessor.insertFiles(TestUtils.OUT, SUFile.COLLECTOR, "files", "fileid,file,version,date");
		}
		catch (Exception e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testProcessTermsAndAttrs()
	{
		try // (SetCollector<SUMOTerm> ignored = SUMOTerm.COLLECTOR.open())
		{
			SuProcessor.insertTermsAndAttrs(TestUtils.OUT, TestUtils.ERR, Term.COLLECTOR, KBLoader.kb, "terms", "sumoid,term", "terms_attr", "sumoid,attr");
		}
		catch (Exception e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testProcessTermAttrs()
	{
		try // (SetCollector<SUMOTerm> ignored = SUMOTerm.COLLECTOR.open())
		{
			SuProcessor.insertTermAttrs(TestUtils.OUT, Term.COLLECTOR, KBLoader.kb, "terms_attr", "sumoid,attr");
		}
		catch (Exception e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testProcessTerms()
	{
		try // (SetCollector<SUMOTerm> ignored = SUMOTerm.COLLECTOR.open())
		{
			SuProcessor.insertTerms(TestUtils.OUT, Term.COLLECTOR, "terms", "sumoid,term");
		}
		catch (Exception e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testProcessFormulasAndArgs()
	{
		try //(
		//SetCollector<SUMOTerm> ignored = SUMOTerm.COLLECTOR.open(); //
		//SetCollector<SUMOFile> ignored2 = SUMOFile.COLLECTOR.open(); //
		//SetCollector<SUMOFormula> ignored3 = SUMOFormula.COLLECTOR.open(); //
		//)
		{
			SuProcessor.insertFormulasAndArgs(TestUtils.OUT, TestUtils.ERR, Formula.COLLECTOR, "formulas", "formulaid,formula,fileid", "formulas_args", "formulaid,sumoid,argtype,argnum");
		}
		catch (Exception e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testProcessFormulas()
	{
		try //(
		//SetCollector<SUMOTerm> ignored = SUMOTerm.COLLECTOR.open(); //
		//SetCollector<SUMOFile> ignored2 = SUMOFile.COLLECTOR.open(); //
		//SetCollector<SUMOFormula> ignored3 = SUMOFormula.COLLECTOR.open(); //
		//)
		{
			SuProcessor.insertFormulas(TestUtils.OUT, Formula.COLLECTOR, "formulas", "formulaid,formula,fileid");
		}
		catch (Exception e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testProcessFormulasArgs()
	{
		try //(
		//SetCollector<SUMOTerm> ignored = SUMOTerm.COLLECTOR.open(); //
		//SetCollector<SUMOFile> ignored2 = SUMOFile.COLLECTOR.open(); //
		//SetCollector<SUMOFormula> ignored3 = SUMOFormula.COLLECTOR.open(); //
		//)
		{
			SuProcessor.insertFormulaArgs(TestUtils.OUT, Formula.COLLECTOR, "formulas_args", "formulaid,sumoid,argtype,argnum");
		}
		catch (Exception e)
		{
			fail(e.getMessage());
		}
	}

	@BeforeAll
	public static void init()
	{
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
		TestProcessor p = new TestProcessor();
		p.testProcessFiles();
		p.testProcessTermsAndAttrs();
		p.testProcessFormulasAndArgs();
		shutdown();
	}
}
