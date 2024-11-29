package org.sqlbuilder.vn.collector

import org.sqlbuilder.common.Logger
import org.sqlbuilder.common.Processor
import org.sqlbuilder.common.Progress.trace
import org.sqlbuilder.common.Progress.traceHeader
import org.sqlbuilder.common.Progress.traceTailer
import org.sqlbuilder.common.XPathUtils.getXPath
import org.sqlbuilder.common.XPathUtils.getXPaths
import org.sqlbuilder.vn.Inherit
import org.sqlbuilder.vn.VnDocument
import org.sqlbuilder.vn.VnModule
import org.sqlbuilder.vn.joins.Class_Frame.Companion.make
import org.sqlbuilder.vn.joins.Class_Word.Companion.make
import org.sqlbuilder.vn.joins.Member_Grouping.Companion.make
import org.sqlbuilder.vn.joins.Member_Sense.Companion.make
import org.sqlbuilder.vn.objects.Frame
import org.sqlbuilder.vn.objects.Member.Companion.make
import org.sqlbuilder.vn.objects.RestrainedRole
import org.sqlbuilder.vn.objects.Role.Companion.make
import org.sqlbuilder.vn.objects.VnClass
import org.sqlbuilder.vn.objects.Word.Companion.make
import org.w3c.dom.Node
import org.xml.sax.SAXException
import java.io.File
import java.io.FilenameFilter
import java.io.IOException
import java.util.*
import javax.xml.parsers.ParserConfigurationException
import javax.xml.transform.TransformerException
import javax.xml.xpath.XPathExpressionException

open class VnCollector(props: Properties) : Processor("vn") {

    @JvmField
    protected val verbNetHome: String = props.getProperty("vn_home", System.getenv()["VNHOME"])

    override fun run() {
        val folder = File(this.verbNetHome)
        val filter = FilenameFilter { _, name -> name.endsWith(".xml") }
        val files = folder.listFiles(filter)
        if (files == null) {
            throw RuntimeException("Dir:" + this.verbNetHome + " is empty")
        }
        // iterate
        var fileCount = 0
        traceHeader("reading verbnet files", "")
        files
            .asSequence()
            .sortedWith(Comparator.comparing<File, String> { it.name })
            .forEach {
                fileCount++
                processVerbNetFile(it.absolutePath, it.name)
                trace(fileCount.toLong())
            }
        traceTailer(fileCount.toLong())
    }

    protected open fun processVerbNetFile(fileName: String, name: String) {
        val head = name.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        try {
            val document = VnDocument(fileName)
            processVerbNetClass(getXPath(document.document, "./VNCLASS")!!, head, null, null)
        } catch (e: ParserConfigurationException) {
            Logger.instance.logXmlException(VnModule.MODULE_ID, tag, fileName, e)
        } catch (e: SAXException) {
            Logger.instance.logXmlException(VnModule.MODULE_ID, tag, fileName, e)
        } catch (e: IOException) {
            Logger.instance.logXmlException(VnModule.MODULE_ID, tag, fileName, e)
        } catch (e: XPathExpressionException) {
            Logger.instance.logXmlException(VnModule.MODULE_ID, tag, fileName, e)
        } catch (e: RuntimeException) {
            System.err.println(fileName)
            throw e
        }
    }

    protected open fun processVerbNetClass(start: Node, head: String, inheritedRestrainedRoles: Collection<RestrainedRole>?, inheritedFrames: Collection<Frame>?) {
        try {
            val clazz: VnClass = processClass(start)
            processItems(start)
            processMembers(start, head, clazz)
            val inheritableRestrainedRoles = processRoles(start, clazz, inheritedRestrainedRoles)
            val inheritableFrames = processFrames(start, clazz, inheritedFrames)

            // recurse
            val subclasses = getXPaths(start, "./SUBCLASSES/VNSUBCLASS")!!
            for (i in 0..<subclasses.length) {
                val subNode = subclasses.item(i)
                processVerbNetClass(subNode, head, inheritableRestrainedRoles, inheritableFrames)
            }
        } catch (e: XPathExpressionException) {
            Logger.instance.logXmlException(VnModule.MODULE_ID, tag, start.ownerDocument.documentURI, e)
        } catch (e: TransformerException) {
            Logger.instance.logXmlException(VnModule.MODULE_ID, tag, start.ownerDocument.documentURI, e)
        } catch (e: ParserConfigurationException) {
            Logger.instance.logXmlException(VnModule.MODULE_ID, tag, start.ownerDocument.documentURI, e)
        } catch (e: SAXException) {
            Logger.instance.logXmlException(VnModule.MODULE_ID, tag, start.ownerDocument.documentURI, e)
        } catch (e: IOException) {
            Logger.instance.logXmlException(VnModule.MODULE_ID, tag, start.ownerDocument.documentURI, e)
        }
    }

    companion object {

        private fun processClass(start: Node): VnClass {
            return VnDocument.makeClass(start)
        }

        @Throws(XPathExpressionException::class, ParserConfigurationException::class, IOException::class, TransformerException::class, SAXException::class)
        private fun processItems(start: Node) {
            // get groupings
            VnDocument.makeGroupings(start)

            // get selection restrs
            VnDocument.makeSelRestrs(start)

            // get syntactic restrs
            VnDocument.makeSynRestrs(start)

            // get selection restr types
            VnDocument.makeSelRestrTypes(start)

            // get syntactic restr types
            VnDocument.makeSynRestrTypes(start)

            // get frame names
            VnDocument.makeFrameNames(start)

            // get frame subnames
            VnDocument.makeFrameSubNames(start)

            // get frame examples
            VnDocument.makeFrameExamples(start)

            // get frame example mappings
            VnDocument.makeFrameExampleMappings(start)

            // get predicates
            VnDocument.makePredicates(start)

            // get predicate semantics mappings
            VnDocument.makePredicateSemanticsMappings(start)

            // get syntaxes
            VnDocument.makeSyntaxes(start)

            // get semantics
            VnDocument.makeSemantics(start)

            // get role types
            VnDocument.makeRoleTypes(start)

            // get roles
            VnDocument.makeRoles(start)

            // get frames
            VnDocument.makeFrames(start)
        }

        @Throws(XPathExpressionException::class, ParserConfigurationException::class, IOException::class, TransformerException::class, SAXException::class)
        private fun processRoles(start: Node, clazz: VnClass, inheritedRestrainedRoles: Collection<RestrainedRole>?): Collection<RestrainedRole> {
            // roles
            var restrainedRoles: MutableCollection<RestrainedRole> = VnDocument.makeRoles(start)
            if (inheritedRestrainedRoles != null) {
                restrainedRoles = Inherit.mergeRoles(restrainedRoles, inheritedRestrainedRoles)
            }

            // collect roles
            for (restrainedRole in restrainedRoles) {
                make(clazz, restrainedRole)
            }

            // return data to be inherited by subclasses
            return restrainedRoles
        }

        @Throws(XPathExpressionException::class, ParserConfigurationException::class, IOException::class, TransformerException::class, SAXException::class)
        private fun processFrames(start: Node, clazz: VnClass, inheritedFrames: Collection<Frame>?): MutableCollection<Frame> {
            // roles
            var frames: MutableCollection<Frame> = VnDocument.makeFrames(start)
            if (inheritedFrames != null) {
                frames = Inherit.mergeFrames(frames, inheritedFrames)
            }

            // collect frames
            for (frame in frames) {
                make(clazz, frame)
            }

            // return data to be inherited by subclasses
            return frames
        }

        @Throws(XPathExpressionException::class)
        private fun processMembers(start: Node, head: String, clazz: VnClass) {
            // members
            val members = VnDocument.makeMembers(start)
            members.add(make(head, null, null))

            // member
            for (member in members) {
                // word
                val word = make(member.lemma)

                // groupings
                if (member.groupings != null) {
                    for (grouping in member.groupings) {
                        make(clazz, word, grouping)
                    }
                }

                // membership
                val membership = make(clazz, word)

                // if sensekeys are null, data apply to all senses
                if (member.senseKeys == null) {
                    // class member sense
                    make(membership, 0, null, 1f)
                    continue
                }

                // else if sensekeys are not null, data apply only to senses pointed at by sensekeys
                for ((i, sensekey) in member.senseKeys.withIndex()) {
                    // sense mapping quality as indicated by verbnet (-prefix to sense key)
                    val senseQuality = sensekey.quality

                    // class member sense
                    make(membership, i, sensekey, senseQuality)
                }
            }
        }
    }
}
