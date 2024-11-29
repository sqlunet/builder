package org.sqlbuilder.vn

import org.sqlbuilder.common.NotNull
import org.sqlbuilder.common.XPathUtils
import org.sqlbuilder.common.XPathUtils.getXPath
import org.sqlbuilder.common.XPathUtils.getXPaths
import org.sqlbuilder.common.XmlTextUtils
import org.sqlbuilder.common.XmlTextUtils.getXPathTexts
import org.sqlbuilder.vn.joins.Frame_Example
import org.sqlbuilder.vn.joins.Frame_Example.Companion.make
import org.sqlbuilder.vn.joins.Predicate_Semantics
import org.sqlbuilder.vn.joins.Predicate_Semantics.Companion.make
import org.sqlbuilder.vn.objects.*
import org.sqlbuilder.vn.objects.Frame.Companion.make
import org.sqlbuilder.vn.objects.Member.Companion.make
import org.sqlbuilder.vn.objects.Member.Companion.makeSensekeys
import org.sqlbuilder.vn.objects.Member.Companion.makeWord
import org.sqlbuilder.vn.objects.RestrType.Companion.make
import org.sqlbuilder.vn.objects.RestrainedRole.Companion.make
import org.sqlbuilder.vn.objects.Restrs.Companion.make
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
        this.document = builder.parse(filePath)
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

        @NotNull
        fun makeClass(start: Node): VnClass {
            val classElement = start as Element
            val className = classElement.getAttribute("ID")
            return VnClass.make(className)
        }

        // M E M B E R S

        @NotNull
        @Throws(XPathExpressionException::class)
        fun makeMembers(start: Node): MutableCollection<Member> {
            val result = ArrayList<Member>()
            val memberNodes = getXPaths(start, "./MEMBERS/MEMBER")
            for (i in 0..<memberNodes!!.length) {
                val memberElement = memberNodes.item(i) as Element
                val lemmaAttribute = memberElement.getAttribute("name")
                val wnAttribute = memberElement.getAttribute("wn")
                val groupingAttribute = memberElement.getAttribute("grouping")
                val member = make(lemmaAttribute, wnAttribute, groupingAttribute)
                result.add(member)
            }
            return result
        }

        @Throws(XPathExpressionException::class)
        fun makeResolvableMembers(start: Node) {
            val memberNodes = getXPaths(start, "./MEMBERS/MEMBER")!!
            for (i in 0..<memberNodes.length) {
                val memberElement = memberNodes.item(i) as Element
                val wordAttribute = memberElement.getAttribute("name")
                val wnAttribute = memberElement.getAttribute("wn")
                makeWord(wordAttribute)
                makeSensekeys(wnAttribute)
                    ?.forEach {
                        Sense.make(it)
                    }
            }
        }

        // G R O U P I N G S

        @NotNull
        @Throws(XPathExpressionException::class)
        fun makeGroupings(start: Node): Set<Grouping> {
            val result = HashSet<Grouping>()
            val memberNodes = getXPaths(start, "./MEMBERS/MEMBER")
            for (i in 0..<memberNodes!!.length) {
                val memberElement = memberNodes.item(i) as Element
                val groupingAttribute = memberElement.getAttribute("grouping")
                if (groupingAttribute.isEmpty()) {
                    continue
                }
                val groupingNames = groupingAttribute.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (groupingName in groupingNames) {
                    val grouping = Grouping.make(groupingName)
                    result.add(grouping)
                }
            }
            return result
        }

        // R O L E

        @NotNull
        @Throws(TransformerException::class, XPathExpressionException::class, IOException::class, SAXException::class, ParserConfigurationException::class)
        fun makeRoles(start: Node): MutableSet<RestrainedRole> {
            val result = HashSet<RestrainedRole>()
            val nodes = getXPaths(start, "./THEMROLES/THEMROLE")
            if (nodes != null) {
                for (i in 0..<nodes.length) {
                    val element = nodes.item(i) as Element
                    val type = element.getAttribute("type")
                    val restrsNodes = getXPaths(element, "./SELRESTRS")
                    val restrsElement = restrsNodes!!.item(0) as Element
                    val selStrsXML = XPathUtils.getXML(restrsElement)
                    result.add(make(type, selStrsXML))
                }
            }
            return result
        }

        @Throws(XPathExpressionException::class)
        fun makeRoleTypes(start: Node): Collection<RoleType> {
            val result = ArrayList<RoleType>()
            val nodes = getXPaths(start, "./THEMROLES/THEMROLE")
            if (nodes != null) {
                for (i in 0..<nodes.length) {
                    val roleElement = nodes.item(i) as Element
                    val type = roleElement.getAttribute("type")
                    result.add(RoleType.make(type))
                }
            }
            return result
        }

        // R E S T R

        @Throws(XPathExpressionException::class)
        fun makeSelRestrTypes(start: Node): Collection<RestrType> {
            val result = ArrayList<RestrType>()
            val selNodes = getXPaths(start, "//SELRESTR")
            if (selNodes != null) {
                for (i in 0..<selNodes.length) {
                    val element = selNodes.item(i) as Element
                    val restrValue = element.getAttribute("Value")
                    val restrType = element.getAttribute("type")
                    val restr = make(restrValue, restrType, false)
                    result.add(restr)
                }
            }
            return result
        }

        @NotNull
        @Throws(XPathExpressionException::class)
        fun makeSynRestrTypes(start: Node): Collection<RestrType> {
            val result = ArrayList<RestrType>()
            val nodes = getXPaths(start, "//SYNRESTR")
            if (nodes != null) {
                for (i in 0..<nodes.length) {
                    val element = nodes.item(i) as Element
                    val restrValue = element.getAttribute("Value")
                    val restrType = element.getAttribute("type")
                    val restr = make(restrValue, restrType, true)
                    result.add(restr)
                }
            }
            return result
        }

        @NotNull
        @Throws(XPathExpressionException::class, TransformerException::class, ParserConfigurationException::class, SAXException::class, IOException::class)
        fun makeSelRestrs(start: Node): Collection<Restrs> {
            val result = ArrayList<Restrs>()
            val nodes = getXPaths(start, "//SELRESTRS")
            if (nodes != null) {
                for (i in 0..<nodes.length) {
                    val element = nodes.item(i) as Element
                    val xML = XPathUtils.getXML(element)
                    if (!xML.isEmpty() && xML != "<SELRESTRS/>") {
                        val restrs = make(xML, false)
                        result.add(restrs)
                    }
                }
            }
            return result
        }

        @NotNull
        @Throws(XPathExpressionException::class, TransformerException::class, ParserConfigurationException::class, SAXException::class, IOException::class)
        fun makeSynRestrs(start: Node): Collection<Restrs> {
            val result = ArrayList<Restrs>()
            val nodes = getXPaths(start, "//SYNRESTRS")
            if (nodes != null) {
                for (i in 0..<nodes.length) {
                    val element = nodes.item(i) as Element
                    val xML = XPathUtils.getXML(element)
                    if (!xML.isEmpty() && xML != "<SYNRESTRS/>") {
                        val restrs = make(xML, true)
                        result.add(restrs)
                    }
                }
            }
            return result
        }

        // F R A M E

        @NotNull
        @Throws(TransformerException::class, XPathExpressionException::class, IOException::class, SAXException::class, ParserConfigurationException::class)
        fun makeFrames(start: Node): MutableCollection<Frame> {
            val result = ArrayList<Frame>()
            val frameNodes = getXPaths(start, "./FRAMES/FRAME")
            for (i in 0..<frameNodes!!.length) {
                val frameElement = frameNodes.item(i) as Element
                val description = getXPath(frameElement, "./DESCRIPTION") as Element?
                val descriptionPrimary = description!!.getAttribute("primary")
                val descriptionSecondary = description.getAttribute("secondary")
                val descriptionNumber = description.getAttribute("descriptionNumber")
                val descriptionXTag = description.getAttribute("xtag")
                val syntax = XPathUtils.getXML(getXPath(frameElement, "./SYNTAX")!!)
                val semantics = XPathUtils.getXML(getXPath(frameElement, "./SEMANTICS")!!)

                val frame = make(descriptionNumber, descriptionXTag, descriptionPrimary, descriptionSecondary, syntax, semantics)
                result.add(frame)
            }
            return result
        }

        @NotNull
        @Throws(XPathExpressionException::class)
        fun makeFrameNames(start: Node): Collection<FrameName> {
            val result = ArrayList<FrameName>()
            val nodes = getXPaths(start, "./FRAMES/FRAME/DESCRIPTION")
            if (nodes != null) {
                for (i in 0..<nodes.length) {
                    val element = nodes.item(i) as Element
                    val name = element.getAttribute("primary")
                    result.add(FrameName.make(name))
                }
            }
            return result
        }

        @NotNull
        @Throws(XPathExpressionException::class)
        fun makeFrameSubNames(start: Node): Collection<FrameSubName?> {
            val result = ArrayList<FrameSubName>()
            val nodes = getXPaths(start, "./FRAMES/FRAME/DESCRIPTION")
            if (nodes != null) {
                for (i in 0..<nodes.length) {
                    val element = nodes.item(i) as Element
                    var subName = element.getAttribute("secondary")
                    if (!subName.isEmpty()) {
                        subName = subName.replace("\\s+".toRegex(), " ")
                        result.add(FrameSubName.make(subName))
                    }
                }
            }
            return result
        }

        @NotNull
        @Throws(XPathExpressionException::class)
        fun makeFrameExamples(start: Node): Collection<FrameExample?> {
            val result = ArrayList<FrameExample>()
            val examples: List<String>? = getXPathTexts(start, "./FRAMES/FRAME/EXAMPLES/EXAMPLE")
            if (examples != null) {
                for (example in examples) {
                    result.add(FrameExample.make(example))
                }
            }
            return result
        }

        @NotNull
        @Throws(TransformerException::class, XPathExpressionException::class, IOException::class, SAXException::class, ParserConfigurationException::class)
        fun makeFrameExampleMappings(start: Node): List<Frame_Example?> {
            val result = ArrayList<Frame_Example?>()
            val frameNodes = getXPaths(start, "./FRAMES/FRAME")
            for (i in 0..<frameNodes!!.length) {
                val frameElement = frameNodes.item(i) as Element
                val description = getXPath(frameElement, "./DESCRIPTION") as Element?
                val descriptionPrimary = description!!.getAttribute("primary")
                val descriptionSecondary = description.getAttribute("secondary")
                val descriptionNumber = description.getAttribute("descriptionNumber")
                val descriptionXTag = description.getAttribute("xtag")
                val syntax = XPathUtils.getXML(getXPath(frameElement, "./SYNTAX")!!)
                val semantics = XPathUtils.getXML(getXPath(frameElement, "./SEMANTICS")!!)

                val frame = make(descriptionNumber, descriptionXTag, descriptionPrimary, descriptionSecondary, syntax, semantics)

                getXPathTexts(frameElement, "./EXAMPLES/EXAMPLE")
                    ?.forEach {
                        val vnExample = FrameExample.make(it)
                        result.add(make(frame, vnExample))
                    }
            }
            return result
        }

        @NotNull
        @Throws(XPathExpressionException::class, TransformerException::class, ParserConfigurationException::class, SAXException::class, IOException::class)
        fun makeSyntaxes(start: Node): Collection<Syntax?> {
            val result = ArrayList<Syntax?>()
            val syntaxes = XmlTextUtils.getXML(getXPaths(start, "./FRAMES/FRAME/SYNTAX")!!)
            // if (syntaxes != null)
            // {
            for (syntax in syntaxes) {
                result.add(Syntax.make(syntax))
            }
            // }
            return result
        }

        @NotNull
        @Throws(XPathExpressionException::class, TransformerException::class, ParserConfigurationException::class, SAXException::class, IOException::class)
        fun makeSemantics(start: Node): Collection<Semantics?> {
            val result = ArrayList<Semantics?>()
            val semanticss = XmlTextUtils.getXML(getXPaths(start, "./FRAMES/FRAME/SEMANTICS")!!)
            // if (semanticss != null)
            // {
            for (semantics in semanticss) {
                result.add(Semantics.make(semantics))
            }
            // }
            return result
        }

        @NotNull
        @Throws(XPathExpressionException::class)
        fun makePredicates(start: Node): Collection<Predicate?> {
            val result = ArrayList<Predicate?>()
            val nodes = getXPaths(start, "./FRAMES/FRAME/SEMANTICS/PRED")
            if (nodes != null) {
                for (i in 0..<nodes.length) {
                    val element = nodes.item(i) as Element
                    val predicate = element.getAttribute("value")
                    result.add(Predicate.make(predicate))
                }
            }
            return result
        }

        @NotNull
        @Throws(TransformerException::class, XPathExpressionException::class, ParserConfigurationException::class, SAXException::class, IOException::class)
        fun makePredicateSemanticsMappings(start: Node): List<Predicate_Semantics> {
            val result = ArrayList<Predicate_Semantics>()
            val semanticsNodes = getXPaths(start, "./FRAMES/FRAME/SEMANTICS")
            for (i in 0..<semanticsNodes!!.length) {
                val semanticNode = semanticsNodes.item(i)
                val semanticsXml = XPathUtils.getXML(semanticNode)
                val semantics = Semantics.make(semanticsXml)

                val predNodes = getXPaths(semanticNode, "./PRED")
                if (predNodes != null) {
                    for (j in 0..<predNodes.length) {
                        val predicateElement = predNodes.item(j) as Element
                        val predicate = Predicate.make(predicateElement.getAttribute("value"))
                        result.add(make(predicate, semantics))
                    }
                }
            }
            return result
        }
    }
}