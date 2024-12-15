package org.semantikos.vn

import org.semantikos.common.XPathUtils.getXML
import org.semantikos.common.XPathUtils.getXPath
import org.semantikos.common.XPathUtils.getXPaths
import org.semantikos.common.XmlProcessor.Companion.iteratorOfElements
import org.semantikos.common.XmlTextUtils.getXPathTexts
import org.semantikos.vn.joins.Frame_Example
import org.semantikos.vn.joins.Frame_Example.Companion.make
import org.semantikos.vn.joins.Predicate_Semantics
import org.semantikos.vn.joins.Predicate_Semantics.Companion.make
import org.semantikos.vn.objects.*
import org.semantikos.vn.objects.Frame.Companion.make
import org.semantikos.vn.objects.Member.Companion.make
import org.semantikos.vn.objects.Member.Companion.makeSensekeys
import org.semantikos.vn.objects.Member.Companion.makeWord
import org.semantikos.vn.objects.RestrType.Companion.make
import org.semantikos.vn.objects.RestrainedRole.Companion.make
import org.semantikos.vn.objects.Restrs.Companion.make
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.xml.sax.EntityResolver
import org.xml.sax.SAXException
import java.io.IOException
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import javax.xml.transform.TransformerException
import javax.xml.xpath.XPathExpressionException

class VnDocument(
    filePath: String,
) {

    var document: Document? = null

    init {
        load(filePath)
    }

    @Throws(ParserConfigurationException::class, SAXException::class, IOException::class)
    private fun load(filePath: String) {
        val builder: DocumentBuilder = makeDocumentBuilder()
        document = builder.parse(filePath)
    }

    companion object {

        @Throws(ParserConfigurationException::class)
        private fun makeDocumentBuilder(): DocumentBuilder {
            val factory = DocumentBuilderFactory.newInstance()
            factory.isCoalescing = true
            factory.isIgnoringComments = true
            factory.isNamespaceAware = false
            factory.isIgnoringElementContentWhitespace = true
            factory.isValidating = false
            factory.isExpandEntityReferences = false

            //factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            val builder = factory.newDocumentBuilder()
            builder.setEntityResolver(EntityResolver { publicId: String?, systemId: String? -> null })
            return builder
        }

        // C L A S S

        fun makeClass(start: Node): VnClass {
            val classElement = start as Element
            val className = classElement.getAttribute("ID")
            return VnClass.make(className)
        }

        // M E M B E R S

        @Throws(XPathExpressionException::class)
        fun makeMembers(start: Node, vararg others: String): Collection<Member> {
            val result = getXPaths(start, "./MEMBERS/MEMBER")!!
                .iteratorOfElements()
                .asSequence()
                .map {
                    val lemmaAttribute = it.getAttribute("name")
                    val wnAttribute = it.getAttribute("wn")
                    val groupingAttribute = it.getAttribute("grouping")
                    make(lemmaAttribute, wnAttribute, groupingAttribute)
                }
                .toMutableList()
            others.forEach {
                result.add(make(it, null, null))
            }
            return result
        }

        @Throws(XPathExpressionException::class)
        fun makeResolvableMembers(start: Node) {
            getXPaths(start, "./MEMBERS/MEMBER")!!
                .iteratorOfElements()
                .forEach {
                    val wordAttribute = it.getAttribute("name")
                    val wnAttribute = it.getAttribute("wn")
                    makeWord(wordAttribute)
                    makeSensekeys(wnAttribute)
                        ?.forEach {
                            Sense.make(it)
                        }
                }
        }

        // G R O U P I N G S

        @Throws(XPathExpressionException::class)
        fun makeGroupings(start: Node): Collection<Grouping> {
            return getXPaths(start, "./MEMBERS/MEMBER")!!
                .iteratorOfElements()
                .asSequence()
                .map { it.getAttribute("grouping") }
                .filter { !it.isEmpty() }
                .flatMap {
                    it
                        .split("\\s".toRegex())
                        .dropLastWhile { it.isEmpty() }
                        .map { Grouping.make(it) }
                }
                .toSet()
        }

        // R O L E

        @Throws(TransformerException::class, XPathExpressionException::class, IOException::class, SAXException::class, ParserConfigurationException::class)
        fun makeRoles(start: Node): Collection<RestrainedRole> {
            return getXPaths(start, "./THEMROLES/THEMROLE")!!
                .iteratorOfElements()
                .asSequence()
                .map {
                    val type = it.getAttribute("type")
                    val restrsElement = getXPaths(it, "./SELRESTRS")?.iteratorOfElements()?.next()
                    val selStrsXML = restrsElement?.getXML()
                    make(type, selStrsXML)
                }
                .toSet()
        }

        @Throws(XPathExpressionException::class)
        fun makeRoleTypes(start: Node): Collection<RoleType> {
            return getXPaths(start, "./THEMROLES/THEMROLE")!!
                .iteratorOfElements()
                .asSequence()
                .map { RoleType.make(it.getAttribute("type")) }
                .toList()
        }

        // R E S T R

        @Throws(XPathExpressionException::class)
        fun makeSelRestrTypes(start: Node): Collection<RestrType> {
            return getXPaths(start, "//SELRESTR")!!
                .iteratorOfElements()
                .asSequence()
                .map {
                    val restrValue = it.getAttribute("Value")
                    val restrType = it.getAttribute("type")
                    make(restrValue, restrType, false)
                }
                .toList()
        }

        @Throws(XPathExpressionException::class)
        fun makeSynRestrTypes(start: Node): Collection<RestrType> {
            return getXPaths(start, "//SYNRESTR")!!
                .iteratorOfElements()
                .asSequence()
                .map {
                    val restrValue = it.getAttribute("Value")
                    val restrType = it.getAttribute("type")
                    make(restrValue, restrType, true)
                }
                .toList()
        }

        @Throws(XPathExpressionException::class, TransformerException::class, ParserConfigurationException::class, SAXException::class, IOException::class)
        fun makeSelRestrs(start: Node): Collection<Restrs> {
            return getXPaths(start, "//SELRESTRS")!!
                .iteratorOfElements()
                .asSequence()
                .map { it.getXML() }
                .filter { !it.isEmpty() && it != "<SELRESTRS/>" }
                .map { make(it, false) }
                .toList()
        }

        @Throws(XPathExpressionException::class, TransformerException::class, ParserConfigurationException::class, SAXException::class, IOException::class)
        fun makeSynRestrs(start: Node): Collection<Restrs> {
            return getXPaths(start, "//SYNRESTRS")!!
                .iteratorOfElements()
                .asSequence()
                .map { it.getXML() }
                .filter { !it.isEmpty() && it != "<SYNRESTRS/>" }
                .map { make(it, true) }
                .toList()
        }

        // F R A M E

        @Throws(TransformerException::class, XPathExpressionException::class, IOException::class, SAXException::class, ParserConfigurationException::class)
        fun makeFrames(start: Node): Collection<Frame> {
            return getXPaths(start, "./FRAMES/FRAME")!!
                .iteratorOfElements()
                .asSequence()
                .map {
                    val description = getXPath(it, "./DESCRIPTION") as Element?
                    val descriptionPrimary = description!!.getAttribute("primary")
                    val descriptionSecondary = description.getAttribute("secondary")
                    val descriptionNumber = description.getAttribute("descriptionNumber")
                    val descriptionXTag = description.getAttribute("xtag")
                    val syntax = getXPath(it, "./SYNTAX")!!.getXML()
                    val semantics = getXPath(it, "./SEMANTICS")!!.getXML()
                    make(descriptionNumber, descriptionXTag, descriptionPrimary, descriptionSecondary, syntax, semantics)
                }
                .toList()
        }

        @Throws(XPathExpressionException::class)
        fun makeFrameNames(start: Node): Collection<FrameName> {
            return getXPaths(start, "./FRAMES/FRAME/DESCRIPTION")!!
                .iteratorOfElements()
                .asSequence()
                .map {
                    val name = it.getAttribute("primary")
                    FrameName.make(name)
                }
                .toList()
        }

        @Throws(XPathExpressionException::class)
        fun makeFrameSubNames(start: Node): Collection<FrameSubName> {
            return getXPaths(start, "./FRAMES/FRAME/DESCRIPTION")!!
                .iteratorOfElements()
                .asSequence()
                .map { it.getAttribute("secondary") }
                .filter { !it.isEmpty() }
                .map {
                    val subName = it.replace("\\s+".toRegex(), " ")
                    FrameSubName.make(subName)
                }
                .toList()
        }

        @Throws(XPathExpressionException::class)
        fun makeFrameExamples(start: Node): Collection<FrameExample>? {
            return getXPathTexts(start, "./FRAMES/FRAME/EXAMPLES/EXAMPLE")
                ?.asSequence()
                ?.map { FrameExample.make(it) }
                ?.toList()
        }

        @Throws(TransformerException::class, XPathExpressionException::class, IOException::class, SAXException::class, ParserConfigurationException::class)
        fun makeFrameExampleMappings(start: Node): Collection<Frame_Example> {
            return getXPaths(start, "./FRAMES/FRAME[EXAMPLES/EXAMPLE]")!!
                .iteratorOfElements()
                .asSequence()
                .flatMap {
                    val description = getXPath(it, "./DESCRIPTION")!! as Element
                    val descriptionPrimary = description.getAttribute("primary")
                    val descriptionSecondary = description.getAttribute("secondary")
                    val descriptionNumber = description.getAttribute("descriptionNumber")
                    val descriptionXTag = description.getAttribute("xtag")
                    val syntax = getXPath(it, "./SYNTAX")!!.getXML()
                    val semantics = getXPath(it, "./SEMANTICS")!!.getXML()
                    val frame = make(descriptionNumber, descriptionXTag, descriptionPrimary, descriptionSecondary, syntax, semantics)

                    getXPathTexts(it, "./EXAMPLES/EXAMPLE")!!
                        .asSequence()
                        .map {
                            val vnExample = FrameExample.make(it)
                            make(frame, vnExample)
                        }
                }
                .toList()
        }

        @Throws(XPathExpressionException::class, TransformerException::class, ParserConfigurationException::class, SAXException::class, IOException::class)
        fun makeSyntaxes(start: Node): Collection<Syntax> {
            return getXPaths(start, "./FRAMES/FRAME/SYNTAX")!!.getXML()
                .asSequence()
                .map { Syntax.make(it) }
                .toList()
        }

        @Throws(XPathExpressionException::class, TransformerException::class, ParserConfigurationException::class, SAXException::class, IOException::class)
        fun makeSemantics(start: Node): Collection<Semantics> {
            return getXPaths(start, "./FRAMES/FRAME/SEMANTICS")!!.getXML()
                .asSequence()
                .map { Semantics.make(it) }
                .toList()
        }

        @Throws(XPathExpressionException::class)
        fun makePredicates(start: Node): Collection<Predicate> {
            return getXPaths(start, "./FRAMES/FRAME/SEMANTICS/PRED")!!
                .iteratorOfElements()
                .asSequence()
                .map {
                    val predicate = it.getAttribute("value")
                    Predicate.make(predicate)
                }
                .toList()
        }

        @Throws(TransformerException::class, XPathExpressionException::class, ParserConfigurationException::class, SAXException::class, IOException::class)
        fun makePredicateSemanticsMappings(start: Node): Collection<Predicate_Semantics> {
            return getXPaths(start, "./FRAMES/FRAME/SEMANTICS")!!
                .iteratorOfElements()
                .asSequence()
                .flatMap {
                    val semanticsXml = it.getXML()
                    val semantics = Semantics.make(semanticsXml)

                    getXPaths(it, "./PRED")!!
                        .iteratorOfElements()
                        .asSequence()
                        .map {
                            val predicate = Predicate.make(it.getAttribute("value"))
                            make(predicate, semantics)
                        }
                }
                .toList()
        }
    }
}
