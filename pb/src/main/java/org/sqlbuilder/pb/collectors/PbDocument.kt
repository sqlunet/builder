package org.sqlbuilder.pb.collectors

import org.sqlbuilder.common.XmlDocument
import org.sqlbuilder.pb.foreign.Alias
import org.sqlbuilder.pb.foreign.VnLinks
import org.sqlbuilder.pb.foreign.VnClass
import org.sqlbuilder.pb.foreign.VnRole
import org.sqlbuilder.pb.foreign.VnRoleAlias
import org.sqlbuilder.pb.objects.*
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import javax.xml.xpath.XPathExpressionException

class PbDocument(filePath: String) : XmlDocument(filePath) {
    companion object {

        fun NodeList.asSequence(): Sequence<Element> {
            return sequence {
                for (i in 0 until length) {
                    yield(item(i) as Element)
                }
            }
        }

        @JvmStatic
        @Throws(XPathExpressionException::class)
        fun getPredicates(head: String, start: Node): Collection<Predicate> {
            return getXPaths(start, "./predicate")
                .asSequence()
                .map {
                    val lemmaAttribute = it.getAttribute("lemma")
                    Predicate.make(head, lemmaAttribute)
                }
                .toList()
        }

        @JvmStatic
        @Throws(XPathExpressionException::class)
        fun getAliasPredicates(start: Node): Collection<LexItem> {
            return getXPaths(start, ".//alias")
                .asSequence()
                .map {
                    val lemma = it.textContent
                    LexItem.make(lemma)
                }
                .toList()
        }

        @JvmStatic
        @Throws(XPathExpressionException::class)
        fun makeRoleSets(head: String, start: Node): Collection<RoleSet> {
            var result: MutableList<RoleSet> = ArrayList<RoleSet>()
            getXPaths(start, "./predicate")
                .asSequence()
                .forEach { predicateElement ->
                    val lemmaAttribute = predicateElement.getAttribute("lemma")
                    val predicate = Predicate.make(head, lemmaAttribute)

                    // predicate as roleset member
                    val pbword = Word.make(predicate.word)

                    getXPaths(predicateElement, "./roleset")
                        .asSequence()
                        .forEach { roleSetElement ->

                            // roleset data
                            val roleSetIdAttribute = roleSetElement.getAttribute("id")
                            val roleSetNameAttribute = roleSetElement.getAttribute("name")

                            // roleset
                            val roleSet = RoleSet.make(predicate, roleSetIdAttribute, roleSetNameAttribute)
                            result.add(roleSet)

                            // roleset member
                            Member.make(roleSet, pbword)

                            // roleset aliases
                            val m = getXPaths(roleSetElement, "./aliases/alias")
                                .asSequence()
                                .map { aliasElement ->

                                    val alias: String = aliasElement.textContent.trim { it <= ' ' }
                                    val pos: String = aliasElement.getAttribute("pos").trim { it <= ' ' }
                                    Word.make(alias) to pos
                                }
                                .toMap()
                            m.keys
                                .asSequence()
                                .forEach {
                                    Member.make(roleSet, it)
                                }

                            // v e r b n e t
                            makeVnRoleSetLinks(roleSetElement)
                                ?.asSequence()
                                ?.forEach {
                                    var clazz = it.trim { it <= ' ' }
                                    if (!clazz.isEmpty() && "-" != clazz) {
                                        m.entries
                                            .asSequence()
                                            .forEach {
                                                val alias = Alias.make(Alias.Db.VERBNET, clazz, it.value, roleSet, it.key)
                                                roleSet.aliases.add(alias)
                                            }
                                    }
                                }

                            // f r a m e n e t
                            makeFnRoleSetLinks(roleSetElement)
                                ?.asSequence()
                                ?.forEach {
                                    var frame = it.trim { it <= ' ' }
                                    if (!frame.isEmpty() && "-" != frame) {
                                        m
                                            .asSequence()
                                            .forEach {
                                                val alias = Alias.make(Alias.Db.FRAMENET, frame, it.value, roleSet, it.key)
                                                roleSet.aliases.add(alias)
                                            }
                                    }
                                }
                        }
                }
            return result
        }

        @JvmStatic
        @Throws(XPathExpressionException::class)
        fun makeVnRoleSetLinks(roleSetElement: Element): Set<String>? {
            return getXPaths(roleSetElement, "./roles/role/rolelinks/rolelink[@resource='VerbNet' and (@version='verbnet3.3' or @version='verbnet3.4')]")
                .asSequence()
                .map { it.getAttribute("class").trim { it <= ' ' } }
                .toSet()
        }

        @JvmStatic
        @Throws(XPathExpressionException::class)
        fun makeFnRoleSetLinks(roleElement: Element): Set<String>? {
            return getXPaths(roleElement, "./roles/role/rolelinks/rolelink[@resource='FrameNet' and @version='1.7']")
                .asSequence()
                .map { it.getAttribute("class").trim { it <= ' ' } }
                .toSet()
        }

        @JvmStatic
        @Throws(XPathExpressionException::class)
        fun makeRoles(head: String, start: Node): Collection<Role> {
            var result: MutableList<Role> = ArrayList<Role>()
            getXPaths(start, "./predicate")
                .asSequence()
                .forEach { predicateElement ->
                    val lemmaAttribute = predicateElement.getAttribute("lemma")
                    val predicate = Predicate.make(head, lemmaAttribute)

                    getXPaths(predicateElement, "./roleset")
                        .asSequence()
                        .forEach { roleSetElement ->

                            // roleset data
                            val roleSetIdAttribute = roleSetElement.getAttribute("id")
                            val nameAttribute = roleSetElement.getAttribute("name")

                            // roleset
                            val roleSet = RoleSet.make(predicate, roleSetIdAttribute, nameAttribute)

                            // roles
                            getXPaths(roleSetElement, "./roles/role")
                                .asSequence()
                                .forEach { roleElement ->

                                    // attributes
                                    val n = roleElement.getAttribute("n")
                                    val f = roleElement.getAttribute("f")
                                    val descriptor = roleElement.getAttribute("descr")

                                    // links
                                    var vnLinks: Set<String>? = makeVnRoleLinks(roleElement)
                                    var fnLinks: Set<String>? = makeFnRoleLinks(roleElement)

                                    // role
                                    val role = Role.make(roleSet, n, f, descriptor, vnLinks, fnLinks)
                                    result.add(role)

                                    // role-vnrole maps
                                    makeVnRoleMaps(head, role, roleElement)
                                }
                        }
                }
            return result
        }

        @JvmStatic
        @Throws(XPathExpressionException::class)
        fun makeVnRoleLinks(roleElement: Element): Set<String>? {
            return getXPaths(roleElement, "./rolelinks/rolelink[@resource='VerbNet' and (@version='verbnet3.3' or @version='verbnet3.4')]")
                .asSequence()
                .map { it.textContent.trim { it <= ' ' } }
                .toSet()
        }

        @JvmStatic
        @Throws(XPathExpressionException::class)
        fun makeFnRoleLinks(roleElement: Element): Set<String>? {
            return getXPaths(roleElement, "./rolelinks/rolelink[@resource='FrameNet' and @version='1.7']")
                .asSequence()
                .map { it.textContent.trim { it <= ' ' } }
                .toSet()
        }

        @Throws(XPathExpressionException::class)
        private fun makeVnRoleMaps(head: String, role: Role, roleElement: Element) {
            getXPaths(roleElement, "./rolelinks/rolelink[@resource='VerbNet']")
                .asSequence()
                .forEach { vnRoleElement ->

                    // extract
                    val vnClassAttribute = vnRoleElement.getAttribute("class")
                    val thetaContent = vnRoleElement.textContent.trim { it <= ' ' }

                    // objects
                    val vnClass = VnClass.make(head, vnClassAttribute)
                    val theta = VnLinks.make(listOf(thetaContent))

                    // verbnet role
                    val vnRole = VnRole.make(vnClass, theta)

                    // propbank role -> verbnet roles
                    VnRoleAlias.make(role, vnRole)
                }
        }

        @JvmStatic
        @Throws(XPathExpressionException::class)
        fun makeExamples(head: String, start: Node): Collection<Example> {
            var result: MutableList<Example> = ArrayList<Example>()
            getXPaths(start, "./predicate")
                .asSequence()
                .forEach { predicateElement ->

                    val lemmaAttribute = predicateElement.getAttribute("lemma")
                    val predicate = Predicate.make(head, lemmaAttribute)

                    getXPaths(predicateElement, "./roleset")
                        .asSequence()
                        .forEach { roleSetElement ->

                            val roleSetIdAttribute = roleSetElement.getAttribute("id")
                            val nameAttribute = roleSetElement.getAttribute("name")
                            val roleSet = RoleSet.make(predicate, roleSetIdAttribute, nameAttribute)

                            getXPaths(roleSetElement, "./example")
                                .asSequence()
                                .forEach { exampleElement ->

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
                                    getXPaths(exampleElement, "./propbank/rel")
                                        .asSequence()
                                        .forEach { relElement ->

                                            val relText: String = relElement.textContent.trim { it <= ' ' }
                                            val rel = Rel.make(example, relText)
                                            example.rels.add(rel)
                                        }

                                    // arguments
                                    getXPaths(exampleElement, "./propbank/arg")
                                        .asSequence()
                                        .forEach { argElement ->

                                            val type = argElement.getAttribute("type")
                                            val argText: String = argElement.textContent.trim { it <= ' ' }
                                            val arg = Arg.make(example, argText, type)
                                            example.args.add(arg)
                                        }
                                    result.add(example)
                                }
                        }
                }
            return result
        }

        @JvmStatic
        @Throws(XPathExpressionException::class)
        fun makeExampleArgs(head: String, start: Node): Collection<Arg> {
            var result = ArrayList<Arg>()
            getXPaths(start, "./predicate")
                .asSequence()
                .forEach { predicateElement ->

                    val lemmaAttribute = predicateElement.getAttribute("lemma")
                    val predicate = Predicate.make(head, lemmaAttribute)

                    getXPaths(predicateElement, "./roleset")
                        .asSequence()
                        .forEach { roleSetElement ->

                            val roleSetIdAttribute = roleSetElement.getAttribute("id")
                            val nameAttribute = roleSetElement.getAttribute("name")
                            val roleSet = RoleSet.make(predicate, roleSetIdAttribute, nameAttribute)

                            getXPaths(roleSetElement, "./example")
                                .asSequence()
                                .forEach { exampleElement ->

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
                                    getXPaths(exampleElement, "./propbank/arg")
                                        .asSequence()
                                        .forEach { argElement ->

                                            val type = argElement.getAttribute("type")
                                            val argText: String = argElement.textContent.trim { it <= ' ' }
                                            val arg = Arg.make(example, argText, type)
                                            result.add(arg)
                                        }
                                }
                        }
                }
            return result
        }
    }
}
