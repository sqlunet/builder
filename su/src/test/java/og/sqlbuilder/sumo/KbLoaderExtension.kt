package og.sqlbuilder.sumo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.sqlbuilder.su.KBLoader;

public class KbLoaderExtension extends KBLoader implements BeforeAllCallback, ExtensionContext.Store.CloseableResource
{
	private static boolean started = false;

	@Override
	public void beforeAll(ExtensionContext context)
	{
		if (!started)
		{
			// The following line registers a callback hook when the root test context is shut down
			context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL).put("com.articulate.sigma.KBloader", this);

			load();
		}
	}

	@Override
	public void close()
	{
		// Your "after all tests" logic goes here
	}

	public void load()
	{
		started = true;

		TestUtils.turnOffLogging();

		// Your "before all tests" startup logic goes here
		kb = loadKb();
		Assertions.assertNotNull(KBLoader.kb);
	}
}

// Then, any tests classes where you need this executed at least once, can be annotated with: @ExtendWith({KBLoader.class})
