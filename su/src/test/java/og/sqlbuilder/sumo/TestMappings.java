package og.sqlbuilder.sumo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sqlbuilder.sumo.KBLoader;
import org.sqlbuilder.sumo.WnProcessor;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

public class TestMappings
{
	private static String kbPath;

	@BeforeAll
	public static void init()
	{
		TestUtils.turnOffLogging();
		kbPath = KBLoader.getPath();
	}

	@Test
	public void testMappings()
	{
		WnProcessor processor = new WnProcessor(kbPath);
		try
		{
			processor.run(TestUtils.OUT,TestUtils.OUT_WARN);
		}
		catch (IOException ioe)
		{
			fail(ioe.getMessage());
		}
	}

	public static void main(String[] args)
	{
		init();
		TestMappings t = new TestMappings();
		t.testMappings();
	}
}
