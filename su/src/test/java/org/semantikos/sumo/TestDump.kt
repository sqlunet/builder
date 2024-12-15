package og.semantikos.sumo

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.semantikos.su.Dump
import org.semantikos.su.KBLoader
import org.semantikos.su.SuProcessor
import org.semantikos.su.objects.Formula
import org.semantikos.su.objects.SUFile
import org.semantikos.su.objects.Term

@ExtendWith(KbLoaderExtension::class)
class TestDump {

    @Test
    fun testDumpTerms() {
        Dump.dumpTerms(KBLoader.kb!!, TestUtils.OUT)
    }

    @Test
    fun testDumpFormulas() {
        Dump.dumpFormulas(KBLoader.kb!!, TestUtils.OUT)
    }

    @Test
    fun testDumpPredicates() {
        Dump.dumpPredicates(KBLoader.kb!!, TestUtils.OUT)
    }

    @Test
    fun testDumpFunctions() {
        Dump.dumpFunctions(KBLoader.kb!!, TestUtils.OUT)
    }

    @Test
    fun testDumpRelations() {
        Dump.dumpFunctions(KBLoader.kb!!, TestUtils.OUT)
    }

    companion object {

        @JvmStatic
        @BeforeAll
        fun init() {
            KBLoader.kb!!.buildRelationCaches()

            SuProcessor.collectFiles(KBLoader.kb!!)
            SuProcessor.collectTerms(KBLoader.kb!!)
            SuProcessor.collectFormulas(KBLoader.kb!!)

            SUFile.COLLECTOR.open()
            Term.COLLECTOR.open()
            Formula.COLLECTOR.open()
        }

        @JvmStatic
        @AfterAll
        fun shutdown() {
            SUFile.COLLECTOR.close()
            Term.COLLECTOR.close()
            Formula.COLLECTOR.close()
        }

        @JvmStatic
        fun main(args: Array<String>) {
            KBLoader().load()

            init()
            val d = TestDump()
            d.testDumpTerms()
            d.testDumpFormulas()
            d.testDumpFormulas()
        }
    }
}
