package org.sqlbuilder.pb.collectors

import org.sqlbuilder.common.XPathUtils.getXPath
import org.sqlbuilder.common.XPathUtils.getXPaths
import org.sqlbuilder.common.XmlDocument
import org.sqlbuilder.common.XmlProcessor.Companion.iteratorOfElements
import org.sqlbuilder.common.XmlTextUtils.getXPathText
import org.sqlbuilder.pb.foreign.RoleSetTo
import org.sqlbuilder.pb.foreign.AliasClass
import org.sqlbuilder.pb.foreign.AliasRole
import org.sqlbuilder.pb.foreign.RoleToVn
import org.sqlbuilder.pb.foreign.Theta
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

        @Throws(XPathExpressionException::class)
        fun getPredicates(head: String, start: Node): Collection<Predicate> {
            return getXPaths(start, "./predicate")!!
                .asSequence()
                .map {
                    val lemmaAttribute = it.getAttribute("lemma")
                    Predicate.make(head, lemmaAttribute)
                }
                .toList()
        }

        @Throws(XPathExpressionException::class)
        fun getAliasPredicates(start: Node): Collection<LexItem> {
            return getXPaths(start, ".//alias")!!
                .asSequence()
                .map {
                    val lemma = it.textContent
                    LexItem.make(lemma)
                }
                .toList()
        }

        @Throws(XPathExpressionException::class)
        fun makeRoleSets(head: String, start: Node): Collection<RoleSet> {
            var result: MutableList<RoleSet> = ArrayList<RoleSet>()
            getXPaths(start, "./predicate")!!
                .asSequence()
                .forEach { predicateElement ->
                    val lemmaAttribute = predicateElement.getAttribute("lemma")
                    val predicate = Predicate.make(head, lemmaAttribute)

                    // predicate as roleset member
                    val pbword = Word.make(predicate.word)

                    getXPaths(predicateElement, "./roleset")!!
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
                                ?.asSequence()
                                ?.map { aliasElement ->

                                    val alias: String = aliasElement.textContent.trim { it <= ' ' }
                                    val pos: String = aliasElement.getAttribute("pos").trim { it <= ' ' }

                                    // alias word
                                    val pbword2 = Word.make(alias)
                                    Word.make(alias)

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

                                            val alias = RoleSetTo.make(RoleSetTo.Db.VERBNET, clazz, pos, roleSet, pbword2)
                                            roleSet.aliases.add(alias)
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
                                            val fnalias = RoleSetTo.make(RoleSetTo.Db.FRAMENET, frame, pos, roleSet, pbword2)
                                            roleSet.aliases.add(fnalias)
                                        }
                                    }

                                    // w o r d   a l i a s
                                    pbword2 to pos
                                }
                                ?.toMap()
                            m?.keys
                                ?.asSequence()
                                ?.forEach {
                                    Member.make(roleSet, it)
                                }
                        }
                }
            return result
        }

        @Throws(XPathExpressionException::class)
        fun makeRoles(head: String, start: Node): Collection<Role> {
            var result: MutableList<Role> = ArrayList<Role>()
            getXPaths(start, "./predicate")
                ?.asSequence()
                ?.forEach { predicateElement ->
                    val lemmaAttribute = predicateElement.getAttribute("lemma")
                    val predicate = Predicate.make(head, lemmaAttribute)

                    getXPaths(predicateElement, "./roleset")!!
                        .asSequence()
                        .forEach { roleSetElement ->

                            // roleset data
                            val roleSetIdAttribute = roleSetElement.getAttribute("id")
                            val nameAttribute = roleSetElement.getAttribute("name")

                            // roleset
                            val roleSet = RoleSet.make(predicate, roleSetIdAttribute, nameAttribute)

                            // roles
                            getXPaths(roleSetElement, "./roles/role")!!
                                .asSequence()
                                .forEach { roleElement ->

                                    // attributes
                                    val n = roleElement.getAttribute("n")
                                    val f = roleElement.getAttribute("f")
                                    val descriptor = roleElement.getAttribute("descr")

                                    // links
                                    var vnLink: String? = null
                                    val vnRoleNodes = getXPaths(roleElement, "./vnrole")
                                    if (vnRoleNodes != null && vnRoleNodes.length > 0) {
                                        val vnRoleElement = vnRoleNodes.item(0) as Element
                                        vnLink = vnRoleElement.getAttribute("vntheta")
                                    }

                                    // role
                                    val role = Role.make(roleSet, n, f, descriptor, vnLink)
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
            getXPaths(roleElement, "./vnrole")!!
                .asSequence()
                .forEach { vnRoleElement ->

                    // extract
                    val vnClassAttribute = vnRoleElement.getAttribute("vncls")
                    val vnClass = AliasClass.make(head, vnClassAttribute)
                    val thetaAttribute = vnRoleElement.getAttribute("vntheta")
                    val theta = Theta.make(thetaAttribute)

                    // verbnet role
                    val vnRole = AliasRole.make(vnClass, theta)

                    // propbank role -> verbnet roles
                    RoleToVn.make(role, vnRole)
                }
        }

        @Throws(XPathExpressionException::class)
        fun makeExamples(head: String, start: Node): Collection<Example> {
            var result: MutableList<Example> = ArrayList<Example>()
            getXPaths(start, "./predicate")!!
                .asSequence()
                .forEach { predicateElement ->

                    val lemmaAttribute = predicateElement.getAttribute("lemma")
                    val predicate = Predicate.make(head, lemmaAttribute)

                    getXPaths(predicateElement, "./roleset")!!
                        .asSequence()
                        .forEach { roleSetElement ->

                            val roleSetIdAttribute = roleSetElement.getAttribute("id")
                            val nameAttribute = roleSetElement.getAttribute("name")
                            val roleSet = RoleSet.make(predicate, roleSetIdAttribute, nameAttribute)

                            getXPaths(roleSetElement, "./example")
                                ?.asSequence()
                                ?.forEach { exampleElement ->

                                    val exampleName = exampleElement.getAttribute("name")
                                    val exampleText = getXPathText(exampleElement, "./text")!!

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
                                    getXPaths(exampleElement, "./rel")!!
                                        .asSequence()
                                        .forEach { relElement ->
                                            val f = relElement.getAttribute("f")
                                            val relText: String = relElement.textContent.trim { it <= ' ' }
                                            val func = Func.make(f)
                                            val rel = Rel.make(example, relText, func)
                                            example.rels.add(rel)
                                        }

                                    // arguments
                                    getXPaths(exampleElement, "./arg")!!
                                        .asSequence()
                                        .forEach { argElement ->
                                            val f = argElement.getAttribute("f")
                                            val n = argElement.getAttribute("n")
                                            val argText: String = argElement.textContent.trim { it <= ' ' }
                                            val arg = Arg.make(example, argText, n, f)
                                            example.args.add(arg)
                                        }
                                    result.add(example)
                                }
                        }
                }
            return result
        }

        @Throws(XPathExpressionException::class)
        fun makeExampleArgs(head: String, start: Node): Collection<Arg> {
            var result = ArrayList<Arg>()
            getXPaths(start, "./predicate")!!
                .asSequence()
                .forEach { predicateElement ->

                    val lemmaAttribute = predicateElement.getAttribute("lemma")
                    val predicate = Predicate.make(head, lemmaAttribute)

                    getXPaths(predicateElement, "./roleset")!!
                        .asSequence()
                        .forEach { roleSetElement ->

                            val roleSetIdAttribute = roleSetElement.getAttribute("id")
                            val nameAttribute = roleSetElement.getAttribute("name")
                            val roleSet = RoleSet.make(predicate, roleSetIdAttribute, nameAttribute)

                            getXPaths(roleSetElement, "./example")
                                ?.asSequence()
                                ?.forEach { exampleElement ->

                                    val exampleName = exampleElement.getAttribute("name")
                                    val exampleText = getXPathText(exampleElement, "./text")!!
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
                                    getXPaths(exampleElement, "./propbank/arg")!!
                                        .asSequence()
                                        .forEach { argElement ->

                                            val f = argElement.getAttribute("f")
                                            val n = argElement.getAttribute("n")
                                            val argText: String = argElement.textContent.trim { it <= ' ' }
                                            val arg = Arg.make(example, argText, n, f)
                                            result.add(arg)
                                        }
                                }
                        }
                }
            return result
        }
    }
}
