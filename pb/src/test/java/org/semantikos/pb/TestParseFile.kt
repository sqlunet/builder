package org.semantikos.pb

import org.junit.Test
import org.semantikos.common.Module.Companion.getProperties
import org.semantikos.pb.collectors.PbCollector
import org.semantikos.pb.foreign.AliasFnFeLinks
import org.semantikos.pb.foreign.AliasVnRoleLinks
import org.semantikos.pb.objects.*
import java.io.File

class TestParseFile {

    @Test
    fun testParse() {
        val path = System.getenv("PARSE")
        println(path)
        val file = File(path)
        val props = getProperties("pb.properties")
        PbCollector(props).processPropBankFile(file.absolutePath, file.getName())

        val funcs = Func.COLLECTOR
        val vnLinks = AliasVnRoleLinks.COLLECTOR
        val fnLinks = AliasFnFeLinks.COLLECTOR
        val rolesets = RoleSet.COLLECTOR
        val roles = Role.COLLECTOR
        val examples = Example.COLLECTOR
        val rels = Rel.COLLECTOR
        val args = Arg.COLLECTOR

        val words = Word.COLLECTOR

        val names = arrayOf("funcs", "vnlinks", "fnlinks", "rolesets", "roles", "examples", "rels", "args", "words")
        val collectors = arrayOf(funcs, vnLinks, fnLinks, rolesets, roles, examples, rels, args, words)
        var i = 0
        for (c in collectors) {
            println(names[i] + " " + c.size)
            i++
        }
    }
}
