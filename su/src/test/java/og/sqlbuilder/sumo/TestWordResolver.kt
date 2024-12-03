package og.sqlbuilder.sumo

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.sqlbuilder.su.KBLoader
import org.sqlbuilder.su.SuWordResolver
import java.io.IOException

class TestWordResolver {

    @Test
    fun testResolver() {
        val id: Int? = resolver.apply("airport")
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

        @Throws(IOException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            KBLoader().load()
            init()
            TestWordResolver().testResolver()
        }
    }
}
