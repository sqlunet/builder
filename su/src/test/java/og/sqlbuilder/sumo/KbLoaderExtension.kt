package og.sqlbuilder.sumo

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource
import org.sqlbuilder.su.KBLoader

class KbLoaderExtension : KBLoader(), BeforeAllCallback, CloseableResource {

    override fun beforeAll(context: ExtensionContext) {
        if (!started) {
            // The following line registers a callback hook when the root test context is shut down
            context.root.getStore(ExtensionContext.Namespace.GLOBAL).put("com.articulate.sigma.KBloader", this)

            load()
        }
    }

    override fun close() {
        // Your "after all tests" logic goes here
    }

    override fun load() {
        started = true

        TestUtils.turnOffLogging()

        // Your "before all tests" startup logic goes here
        kb = loadKb()
        Assertions.assertNotNull(kb)
    }

    companion object {

        private var started = false
    }
}

