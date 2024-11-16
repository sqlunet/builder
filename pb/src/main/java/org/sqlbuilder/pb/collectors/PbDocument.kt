package org.sqlbuilder.pb.collectors

import org.sqlbuilder.common.XmlDocument
import org.sqlbuilder.pb.foreign.Alias
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
                            val aliases = roleSet.aliases
                            getXPaths(roleSetElement, "./aliases/alias")
                                .asSequence()
                                .forEach { aliasElement ->

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
            return result
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

                                    // theta
                                    var theta: List<String>? = makeThetas(roleElement)

                                    // role
                                    val role = Role.make(roleSet, n, f, descriptor, theta?.joinToString(separator = " "))
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
        fun makeThetas(roleElement: Element): List<String>? {
            return getXPaths(roleElement, "./rolelinks/rolelink[@resource='VerbNet']")
                .asSequence()
                .map { it.textContent.trim { it <= ' ' } }
                .toList()
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
                    val theta = Theta.make(thetaContent)

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
