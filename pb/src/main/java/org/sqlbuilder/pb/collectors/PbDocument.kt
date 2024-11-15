package org.sqlbuilder.pb.collectors

import org.sqlbuilder.common.XmlDocument
import org.sqlbuilder.pb.foreign.Alias
import org.sqlbuilder.pb.foreign.VnClass
import org.sqlbuilder.pb.foreign.VnRole
import org.sqlbuilder.pb.foreign.VnRoleAlias
import org.sqlbuilder.pb.objects.Arg
import org.sqlbuilder.pb.objects.Example
import org.sqlbuilder.pb.objects.Func
import org.sqlbuilder.pb.objects.LexItem
import org.sqlbuilder.pb.objects.Member
import org.sqlbuilder.pb.objects.Predicate
import org.sqlbuilder.pb.objects.Rel
import org.sqlbuilder.pb.objects.Role
import org.sqlbuilder.pb.objects.RoleSet
import org.sqlbuilder.pb.objects.Theta
import org.sqlbuilder.pb.objects.Word
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.util.ArrayList
import javax.xml.xpath.XPathExpressionException

class PbDocument(filePath: String?) : XmlDocument(filePath) {
    companion object {

        @JvmStatic
        @Throws(XPathExpressionException::class)
        fun getPredicates(head: String, start: Node): MutableCollection<Predicate>? {
            var result: MutableList<Predicate>? = null
            val predicateNodes = getXPaths(start, "./predicate")
            for (i in 0 until predicateNodes.length) {
                val predicateElement = predicateNodes.item(i) as Element
                val lemmaAttribute = predicateElement.getAttribute("lemma")
                val predicate = Predicate.make(head, lemmaAttribute)
                if (result == null) {
                    result = ArrayList<Predicate>()
                }
                result.add(predicate)
            }
            return result
        }

        @JvmStatic
        @Throws(XPathExpressionException::class)
        fun getAliasPredicates(start: Node): MutableCollection<LexItem>? {
            var result: MutableList<LexItem>? = null
            val aliasNodes = getXPaths(start, ".//alias")
            for (i in 0 until aliasNodes.length) {
                val aliasElement = aliasNodes.item(i) as Element
                val lemma = aliasElement.textContent // $NON-NLS-1$
                val predicate = LexItem.make(lemma)
                if (result == null) {
                    result = ArrayList<LexItem>()
                }
                result.add(predicate)
            }
            return result
        }

        @JvmStatic
        @Throws(XPathExpressionException::class)
        fun makeRoleSets(head: String, start: Node): MutableCollection<RoleSet>? {
            var result: MutableList<RoleSet>? = null
            val predicateNodes = getXPaths(start, "./predicate")
            for (i in 0 until predicateNodes.length) {
                val predicateElement = predicateNodes.item(i) as Element
                val lemmaAttribute = predicateElement.getAttribute("lemma")
                val predicate = Predicate.make(head, lemmaAttribute)

                // predicate as roleset member
                val pbword = Word.make(predicate.word)

                val roleSetNodes = getXPaths(predicateElement, "./roleset")
                for (j in 0 until roleSetNodes.length) {
                    // roleset data
                    val roleSetElement = roleSetNodes.item(j) as Element
                    val roleSetIdAttribute = roleSetElement.getAttribute("id")
                    val roleSetNameAttribute = roleSetElement.getAttribute("name")

                    // roleset
                    val roleSet = RoleSet.make(predicate, roleSetIdAttribute, roleSetNameAttribute)
                    if (result == null) {
                        result = ArrayList<RoleSet>()
                    }
                    result.add(roleSet)

                    // roleset member
                    Member.make(roleSet, pbword)

                    // roleset aliases
                    val aliases = roleSet.aliases
                    val aliasRoleNodes = getXPaths(roleSetElement, "./aliases/alias")
                    if (aliasRoleNodes != null && aliasRoleNodes.length > 0) {
                        for (l in 0 until aliasRoleNodes.length) {
                            val aliasElement = aliasRoleNodes.item(l) as Element
                            val pos: String = aliasElement.getAttribute("pos").trim { it <= ' ' }
                            val word2: String = aliasElement.textContent.trim { it <= ' ' }

                            // alias word
                            val pbword2 = Word.make(word2)

                            // alias word as roleset member
                            Member.make(roleSet, pbword2)

                            // v e r b n e t
                            val verbNet: String = aliasElement.getAttribute("verbnet").trim { it <= ' ' }
                            if (!verbNet.isEmpty()) {
                                val classes: Array<String> = verbNet.split("[\\s,]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                for (clazz in classes) {
                                    var clazz = clazz
                                    clazz = clazz.trim { it <= ' ' }
                                    if (clazz.isEmpty() || "-" == clazz) {
                                        continue
                                    }
                                    if (clazz.matches("^[a-z]+-.*$".toRegex())) {
                                        //System.err.print('\n' + clazz);
                                        clazz = clazz.replaceFirst("^[a-z]+-".toRegex(), "")
                                        //System.err.println('>' + clazz);
                                    }

                                    val alias = Alias.make(Alias.Db.VERBNET, clazz, pos, roleSet, pbword2)
                                    aliases.add(alias)
                                }
                            }

                            // f r a m e n e t
                            val frameNet: String = aliasElement.getAttribute("framenet").trim { it <= ' ' }
                            if (!frameNet.isEmpty()) {
                                val frames: Array<String> = frameNet.split("[\\s,]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                for (frame in frames) {
                                    var frame = frame
                                    frame = frame.trim { it <= ' ' }
                                    if (frame.isEmpty() || "-" == frame) {
                                        continue
                                    }
                                    val alias = Alias.make(Alias.Db.FRAMENET, frame, pos, roleSet, pbword2)
                                    aliases.add(alias)
                                }
                            }
                        }
                    }
                }
            }
            return result
        }

        @JvmStatic
        @Throws(XPathExpressionException::class)
        fun makeRoles(head: String, start: Node): MutableCollection<Role>? {
            var result: MutableList<Role>? = null
            val predicateNodes = getXPaths(start, "./predicate")
            for (i in 0 until predicateNodes.length) {
                val predicateElement = predicateNodes.item(i) as Element
                val lemmaAttribute = predicateElement.getAttribute("lemma")
                val predicate = Predicate.make(head, lemmaAttribute)

                val roleSetNodes = getXPaths(predicateElement, "./roleset")
                for (j in 0 until roleSetNodes.length) {
                    // roleset data
                    val roleSetElement = roleSetNodes.item(j) as Element
                    val roleSetIdAttribute = roleSetElement.getAttribute("id")
                    val nameAttribute = roleSetElement.getAttribute("name")

                    // roleset
                    val roleSet = RoleSet.make(predicate, roleSetIdAttribute, nameAttribute)

                    val roleNodes = getXPaths(roleSetElement, "./roles/role")
                    for (k in 0 until roleNodes.length) {
                        val roleElement = roleNodes.item(k) as Element

                        // attributes
                        val nAttribute = roleElement.getAttribute("n")
                        val fAttribute = roleElement.getAttribute("f")
                        val descriptorAttribute = roleElement.getAttribute("descr")
                        var thetaAttribute: String? = null
                        val vnRoleNodes = getXPaths(roleElement, "./vnrole")
                        if (vnRoleNodes != null && vnRoleNodes.length > 0) {
                            val vnRoleElement = vnRoleNodes.item(0) as Element
                            thetaAttribute = vnRoleElement.getAttribute("vntheta")
                        }

                        // role
                        val role = Role.make(roleSet, nAttribute, fAttribute, descriptorAttribute, thetaAttribute)
                        if (result == null) {
                            result = ArrayList<Role>()
                        }
                        result.add(role)

                        // role-vnrole maps
                        makeVnRoleMaps(head, role, roleElement)
                    }
                }
            }
            return result
        }

        @Throws(XPathExpressionException::class)
        private fun makeVnRoleMaps(head: String, role: Role, roleElement: Element) {
            val vnRoleNodes = getXPaths(roleElement, "./vnrole")
            for (l in 0 until vnRoleNodes.length) {
                val vnRoleElement = vnRoleNodes.item(l) as Element
                val vnClassAttribute = vnRoleElement.getAttribute("vncls")
                val vnClass = VnClass.make(head, vnClassAttribute)
                val thetaAttribute = vnRoleElement.getAttribute("vntheta")
                val theta = Theta.make(thetaAttribute)

                // verbnet role
                val vnRole = VnRole.make(vnClass, theta)

                // propbank role -> verbnet roles
                VnRoleAlias.make(role, vnRole)
            }
        }

        @JvmStatic
        @Throws(XPathExpressionException::class)
        fun makeExamples(head: String, start: Node?): MutableCollection<Example>? {
            var result: MutableList<Example>? = null
            val predicateNodes = getXPaths(start, "./predicate")
            for (i in 0 until predicateNodes.length) {
                val predicateElement = predicateNodes.item(i) as Element
                val lemmaAttribute = predicateElement.getAttribute("lemma")
                val predicate = Predicate.make(head, lemmaAttribute)

                val roleSetNodes = getXPaths(predicateElement, "./roleset")
                for (j in 0 until roleSetNodes.length) {
                    val roleSetElement = roleSetNodes.item(j) as Element
                    val roleSetIdAttribute = roleSetElement.getAttribute("id")
                    val nameAttribute = roleSetElement.getAttribute("name")
                    val roleSet = RoleSet.make(predicate, roleSetIdAttribute, nameAttribute)

                    val exampleNodes = getXPaths(roleSetElement, "./example")
                    for (k in 0 until exampleNodes.length) {
                        val exampleElement = exampleNodes.item(k) as Element

                        val exampleName = exampleElement.getAttribute("name")
                        val exampleText = getXPathText(exampleElement, "./text")

                        var aspect: String? = null
                        var form: String? = null
                        var person: String? = null
                        var tense: String? = null
                        var voice: String? = null
                        val inflectionNode = getXPath(exampleElement, "./inflection")
                        if (inflectionNode != null) {
                            val inflectionElement = inflectionNode as Element
                            aspect = inflectionElement.getAttribute("aspect")
                            form = inflectionElement.getAttribute("form")
                            person = inflectionElement.getAttribute("person")
                            tense = inflectionElement.getAttribute("tense")
                            voice = inflectionElement.getAttribute("voice")
                        }
                        var example = Example.make(roleSet, exampleName, exampleText, aspect, form, person, tense, voice)

                        // relations
                        val relNodes = getXPaths(exampleElement, "./rel")
                        for (l in 0 until relNodes.length) {
                            val relElement = relNodes.item(l) as Element

                            val f = relElement.getAttribute("f")
                            val relText: String = relElement.textContent.trim { it <= ' ' }
                            val func = Func.make(f)
                            val rel = Rel.make(example, relText, func)
                            example.rels.add(rel)
                        }

                        // arguments
                        val argNodes = getXPaths(exampleElement, "./arg")
                        for (m in 0 until argNodes.length) {
                            val argElement = argNodes.item(m) as Element

                            val f = argElement.getAttribute("f")
                            val n = argElement.getAttribute("n")
                            val argText: String = argElement.textContent.trim { it <= ' ' }
                            val arg = Arg.make(example, argText, n, f)
                            example.args.add(arg)
                        }

                        if (result == null) {
                            result = ArrayList<Example>()
                        }
                        result.add(example)
                    }
                }
            }
            return result
        }

        @JvmStatic
        @Throws(XPathExpressionException::class)
        fun makeExampleArgs(head: String, start: Node?): MutableCollection<Arg>? {
            var result: MutableList<Arg>? = null
            val predicateNodes = getXPaths(start, "./predicate")
            for (i in 0 until predicateNodes.length) {
                val predicateElement = predicateNodes.item(i) as Element
                val lemmaAttribute = predicateElement.getAttribute("lemma")
                val predicate = Predicate.make(head, lemmaAttribute)

                val roleSetNodes = getXPaths(predicateElement, "./roleset")
                for (j in 0 until roleSetNodes.length) {
                    val roleSetElement = roleSetNodes.item(j) as Element
                    val roleSetIdAttribute = roleSetElement.getAttribute("id")
                    val nameAttribute = roleSetElement.getAttribute("name")
                    val roleSet = RoleSet.make(predicate, roleSetIdAttribute, nameAttribute)

                    val exampleNodes = getXPaths(roleSetElement, "./example")
                    for (k in 0 until exampleNodes.length) {
                        val exampleElement = exampleNodes.item(k) as Element

                        val exampleName = exampleElement.getAttribute("name")
                        val exampleText = getXPathText(exampleElement, "./text")

                        var aspect: String? = null
                        var form: String? = null
                        var person: String? = null
                        var tense: String? = null
                        var voice: String? = null
                        val inflectionNode = getXPath(exampleElement, "./inflection")
                        if (inflectionNode != null) {
                            val inflectionElement = inflectionNode as Element
                            aspect = inflectionElement.getAttribute("aspect")
                            form = inflectionElement.getAttribute("form")
                            person = inflectionElement.getAttribute("person")
                            tense = inflectionElement.getAttribute("tense")
                            voice = inflectionElement.getAttribute("voice")
                        }
                        var example = Example.make(roleSet, exampleName, exampleText, aspect, form, person, tense, voice)

                        // args
                        val argNodes = getXPaths(exampleElement, "./arg")
                        for (l in 0 until argNodes.length) {
                            val argElement = argNodes.item(l) as Element

                            val f = argElement.getAttribute("f")
                            val n = argElement.getAttribute("n")
                            val argText: String = argElement.textContent.trim { it <= ' ' }
                            val arg = Arg.make(example, argText, n, f)
                            if (result == null) {
                                result = ArrayList<Arg>()
                            }
                            result.add(arg)
                        }
                    }
                }
            }
            return result
        }
    }
}