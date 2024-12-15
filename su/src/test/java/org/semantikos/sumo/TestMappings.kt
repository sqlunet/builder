package og.semantikos.sumo

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.semantikos.su.KBLoader
import org.semantikos.su.KBLoader.Companion.path
import org.semantikos.su.SuProcessor
import org.semantikos.su.SuProcessor.Companion.collectSynsets
import org.semantikos.su.joins.Term_Synset
import org.semantikos.su.objects.Term
import java.io.File
import java.io.IOException
import kotlin.Throws

@ExtendWith(KbLoaderExtension::class)
class TestMappings {

    @Test
    fun testMappings() {
        try {
            SuProcessor.insertSynsets(TestUtils.OUT, Term_Synset.SET, "terms_senses", "terms_senses")
        } catch (e: Exception) {
            Assertions.fail<Any?>(e.message)
        }
    }

    companion object {

        @JvmStatic
        @BeforeAll
        @Throws(IOException::class)
        fun init() {
            val kbPath = path
            SuProcessor.collectTerms(KBLoader.kb!!)
            collectSynsets(kbPath + File.separator + SuProcessor.SUMO_TEMPLATE, TestUtils.OUT_WARN)

            Term.COLLECTOR.open()
        }

        @JvmStatic
        @AfterAll
        fun shutdown() {
            Term.COLLECTOR.close()
        }

        @JvmStatic
        @Throws(IOException::class)
        fun main(args: Array<String>) {
            KBLoader().load()
            init()
            val t = TestMappings()
            t.testMappings()
            shutdown()
        }
    }
}
