package og.semantikos.sumo

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.semantikos.su.KBLoader
import org.semantikos.su.SuProcessor
import org.semantikos.su.objects.Formula
import org.semantikos.su.objects.SUFile
import org.semantikos.su.objects.Term

@ExtendWith(KbLoaderExtension::class)
class TestProcessor {

    @Test
    fun testProcessFiles() {
        try {
            //SUFile.COLLECTOR.open().use {
                SuProcessor.insertFiles(TestUtils.OUT, SUFile.COLLECTOR, "files", "fileid,file,version,date")
            //}
        } catch (e: Exception) {
            Assertions.fail<Any?>(e.message)
        }
    }

    @Test
    fun testProcessTermsAndAttrs() {
        try {
            //Term.COLLECTOR.open().use {
                SuProcessor.insertTermsAndAttrs(TestUtils.OUT, TestUtils.ERR, Term.COLLECTOR, KBLoader.kb!!, "terms", "sumoid,term", "terms_attr", "sumoid,attr")
            //}
        } catch (e: Exception) {
            Assertions.fail<Any?>(e.message)
        }
    }

    @Test
    fun testProcessTermAttrs() {
        try {
            //Term.COLLECTOR.open().use {
                SuProcessor.insertTermAttrs(TestUtils.OUT, Term.COLLECTOR, KBLoader.kb!!, "terms_attr", "sumoid,attr")
            //}
        } catch (e: Exception) {
            Assertions.fail<Any?>(e.message)
        }
    }

    @Test
    fun testProcessTerms() {
        try {
            //Term.COLLECTOR.open().use {
                SuProcessor.insertTerms(TestUtils.OUT, Term.COLLECTOR, "terms", "sumoid,term")
            //}
        } catch (e: Exception) {
            Assertions.fail<Any?>(e.message)
        }
    }

    @Test
    fun testProcessFormulasAndArgs() {
        try {
            //Term.COLLECTOR.open().use {
            //    SUFile.COLLECTOR.open().use {
            //        Formula.COLLECTOR.open().use {
                        SuProcessor.insertFormulasAndArgs(TestUtils.OUT, TestUtils.ERR, Formula.COLLECTOR, "formulas", "formulaid,formula,fileid", "formulas_args", "formulaid,sumoid,argtype,argnum")
            //        }
            //    }
            //}
        } catch (e: Exception) {
            Assertions.fail<Any?>(e.message)
        }
    }

    @Test
    fun testProcessFormulas() {
        try {
            //Term.COLLECTOR.open().use {
            //    SUFile.COLLECTOR.open().use {
            //        Formula.COLLECTOR.open().use {
                        SuProcessor.insertFormulas(TestUtils.OUT, Formula.COLLECTOR, "formulas", "formulaid,formula,fileid")
            //        }
            //    }
            //}
        } catch (e: Exception) {
            Assertions.fail<Any?>(e.message)
        }
    }

    @Test
    fun testProcessFormulasArgs() {
        try {
            //Term.COLLECTOR.open().use {
            //    SUFile.COLLECTOR.open().use {
            //        Formula.COLLECTOR.open().use {
                        SuProcessor.insertFormulaArgs(TestUtils.OUT, Formula.COLLECTOR, "formulas_args", "formulaid,sumoid,argtype,argnum")
            //        }
            //    }
            //}
        } catch (e: Exception) {
            Assertions.fail<Any?>(e.message)
        }
    }

    companion object {

        @JvmStatic
        @BeforeAll
        fun init() {
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
            val p = TestProcessor()
            p.testProcessFiles()
            p.testProcessTermsAndAttrs()
            p.testProcessFormulasAndArgs()
            shutdown()
        }
    }
}
