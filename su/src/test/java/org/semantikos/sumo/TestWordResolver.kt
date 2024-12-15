package og.semantikos.sumo

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.semantikos.su.KBLoader
import org.semantikos.su.SuWordResolver
import java.io.IOException

class TestWordResolver {

    @Test
    fun testResolver() {
        val id: Int? = resolver.invoke("airport")
        println(id)
    }

    companion object {

        lateinit var resolver: SuWordResolver

        @JvmStatic
        @BeforeAll
        fun init() {
            val ser: String = System.getenv()["SUWORDRESOLVER"]!!
            resolver = SuWordResolver(ser)
        }

        @JvmStatic
        @Throws(IOException::class)
        fun main(args: Array<String>) {
            KBLoader().load()
            init()
            TestWordResolver().testResolver()
        }
    }
}
