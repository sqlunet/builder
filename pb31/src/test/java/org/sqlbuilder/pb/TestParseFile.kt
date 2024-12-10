package org.sqlbuilder.pb

import org.junit.Test
import org.sqlbuilder.common.Module
import org.sqlbuilder.pb.collectors.PbCollector
import org.sqlbuilder.pb.objects.*
import java.io.File

class TestParseFile {

    @Test
    fun testParse() {
        val path = System.getenv("PARSE")
        println(path)
        val file = File(path)
        val props = Module.getProperties("pb.properties")
        PbCollector(props).processPropBankFile(file.absolutePath, file.getName())

        val aspects = Example.ASPECT_COLLECTOR
        val forms = Example.FORM_COLLECTOR
        val persons = Example.PERSON_COLLECTOR
        val tenses = Example.TENSE_COLLECTOR
        val voices = Example.VOICE_COLLECTOR
        val funcs = Func.COLLECTOR
        val thetas = Theta.COLLECTOR

        val rolesets = RoleSet.COLLECTOR
        val roles = Role.COLLECTOR
        val examples = Example.COLLECTOR
        val rels = Rel.COLLECTOR
        val args = Arg.COLLECTOR

        val words = Word.COLLECTOR

        val names = arrayOf("aspects", "forms", "persons", "tenses", "voices", "funcs", "thetas", "rolesets", "roles", "examples", "rels", "args", "words")
        val collectors = arrayOf(aspects, forms, persons, tenses, voices, funcs, thetas, rolesets, roles, examples, rels, args, words)
        var i = 0
        for (c in collectors) {
            println(names[i] + " " + c.size)
            i++
        }
    }
}
