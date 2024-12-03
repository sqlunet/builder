package og.sqlbuilder.sumo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sqlbuilder.su.KBLoader;
import org.sqlbuilder.su.SuProcessor;
import org.sqlbuilder.su.joins.Term_Synset;
import org.sqlbuilder.su.objects.Term;

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
			SuProcessor.insertSynsets(TestUtils.OUT, Term_Synset.SET, "terms_senses", "terms_senses");
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
		SuProcessor.collectTerms(KBLoader.kb);
		SuProcessor.collectSynsets(kbPath + File.separator + SuProcessor.SUMO_TEMPLATE, TestUtils.OUT_WARN);

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
