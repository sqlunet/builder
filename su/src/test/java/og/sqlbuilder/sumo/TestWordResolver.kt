package og.sqlbuilder.sumo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sqlbuilder.su.KBLoader;
import org.sqlbuilder.su.SuWordResolver;

import java.io.IOException;

public class TestWordResolver
{
	static SuWordResolver resolver;

	@BeforeAll
	public static void init()
	{
		final String ser = System.getenv().get("SUWORDRESOLVER");
		resolver = new SuWordResolver(ser);
	}

	@Test
	public void testResolver()
	{
		var id = resolver.apply("airport");
		System.out.println(id);
	}

	public static void main(String[] args) throws IOException
	{
		new KBLoader().load();
		init();
		new TestWordResolver().testResolver();
	}
}
