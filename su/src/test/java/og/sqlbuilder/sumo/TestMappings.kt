package og.sqlbuilder.sumo

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.sqlbuilder.su.KBLoader
import org.sqlbuilder.su.KBLoader.Companion.path
import org.sqlbuilder.su.SuProcessor
import org.sqlbuilder.su.SuProcessor.Companion.collectSynsets
import org.sqlbuilder.su.joins.Term_Synset
import org.sqlbuilder.su.objects.Term
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

        @BeforeAll
        @Throws(IOException::class)
        fun init() {
            val kbPath = path
            SuProcessor.collectTerms(KBLoader.kb!!)
            collectSynsets(kbPath + File.separator + SuProcessor.SUMO_TEMPLATE, TestUtils.OUT_WARN)

            Term.COLLECTOR.open()
        }

        @AfterAll
        fun shutdown() {
            Term.COLLECTOR.close()
        }

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
