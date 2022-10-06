package og.sqlbuilder.sumo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sqlbuilder.sumo.KBLoader;
import org.sqlbuilder.sumo.SumoProcessor;
import org.sqlbuilder.sumo.joins.Term_Synset;
import org.sqlbuilder.sumo.objects.Term;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith({KbLoaderExtension.class})
public class TestMappings
{
	@Test
	public void testMappings()
	{
		try
		{
			SumoProcessor.insertSynsets(TestUtils.OUT, Term_Synset.SET, "terms_senses", "terms_senses");
		}
		catch (Exception e)
		{
			fail(e.getMessage());
		}
	}

	@BeforeAll
	public static void init() throws IOException
	{
		String kbPath = KBLoader.getPath();
		SumoProcessor.collectTerms(KBLoader.kb);
		SumoProcessor.collectSynsets(kbPath + File.separator + SumoProcessor.SUMO_TEMPLATE, TestUtils.OUT_WARN);

		Term.COLLECTOR.open();
	}

	@AfterAll
	public static void shutdown()
	{
		Term.COLLECTOR.close();
	}

	public static void main(String[] args) throws IOException
	{
		new KBLoader().load();
		init();
		TestMappings t = new TestMappings();
		t.testMappings();
		shutdown();
	}
}
