package org.sqlbuilder.vn.collector

import org.sqlbuilder.common.Logger
import org.sqlbuilder.common.XPathUtils.getXPaths
import org.sqlbuilder.vn.Inherit
import org.sqlbuilder.vn.VnDocument
import org.sqlbuilder.vn.VnModule
import org.sqlbuilder.vn.objects.Frame
import org.sqlbuilder.vn.objects.Member.Companion.make
import org.sqlbuilder.vn.objects.RestrainedRole
import org.sqlbuilder.vn.objects.Role.Companion.make
import org.sqlbuilder.vn.objects.VnClass
import org.sqlbuilder.vn.objects.Word.Companion.make
import org.w3c.dom.Node
import org.xml.sax.SAXException
import java.io.IOException
import java.util.*
import javax.xml.parsers.ParserConfigurationException
import javax.xml.transform.TransformerException
import javax.xml.xpath.XPathExpressionException

class VnExportCollector(props: Properties) : VnCollector(props) {

    override fun processVerbNetClass(start: Node, head: String, inheritedRestrainedRoles: Collection<RestrainedRole>?, ignored: Collection<Frame>?) {
        try {
            val clazz: VnClass = processClass(start)
            processItems(start)
            processMembers(start, head)
            val inheritableRestrainedRoles = processRoles(start, clazz, inheritedRestrainedRoles)

            // recurse
            val subclasses = getXPaths(start, "./SUBCLASSES/VNSUBCLASS")
            for (i in 0..<subclasses!!.length) {
                val subNode = subclasses.item(i)
                processVerbNetClass(subNode, head, inheritableRestrainedRoles, ignored)
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

        @Throws(XPathExpressionException::class)
        private fun processMembers(start: Node, head: String) {
            // members
            val members = VnDocument.makeMembers(start)
            members.add(make(head, null, null))

            // member
            for (member in members) {
                // word
                make(member.lemma)
            }
        }

        @Throws(XPathExpressionException::class, ParserConfigurationException::class, IOException::class, TransformerException::class, SAXException::class)
        private fun processItems(start: Node) {
            // get role types
            VnDocument.makeRoleTypes(start)

            // get roles
            VnDocument.makeRoles(start)
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
    }
}
