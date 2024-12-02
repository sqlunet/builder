package org.sqlbuilder.sl.collectors

import org.sqlbuilder.common.XPathUtils.getXPaths
import org.sqlbuilder.common.XmlDocument
import org.sqlbuilder.common.XmlProcessor.Companion.iteratorOfElements
import org.sqlbuilder.sl.foreign.PbRole
import org.sqlbuilder.sl.foreign.PbRoleSet_VnClass
import org.sqlbuilder.sl.foreign.VnRole.Companion.make
import org.sqlbuilder.sl.foreign.PbRole_VnRole.Companion.make
import org.sqlbuilder.sl.objects.Predicate
import org.sqlbuilder.sl.objects.Theta
import org.w3c.dom.Node
import javax.xml.xpath.XPathExpressionException

/*
<predicate lemma="abandon">
    <argmap pb-roleset="abandon.01" vn-class="51.2">
        <role pb-arg="0" vn-theta="Theme" />
    </argmap>
</predicate>
*/

class SemlinkDocument(filePath: String) : XmlDocument(filePath) {

    companion object {

        @JvmStatic
        @Throws(XPathExpressionException::class)
        fun makeMappings(start: Node) {
            getXPaths(start, "./predicate")!!
                .iteratorOfElements()
                .asSequence()
                .forEach { predicateElement ->
                    val lemmaAttribute = predicateElement.getAttribute("lemma")
                    Predicate.make(lemmaAttribute)

                    getXPaths(predicateElement, "./argmap")!!
                        .iteratorOfElements()
                        .asSequence()
                        .forEach { argmapElement ->

                            // propbank roleset
                            val roleSetAttribute = argmapElement.getAttribute("pb-roleset")

                            // verbnet class
                            val vnClassAttribute = argmapElement.getAttribute("vn-class")

                            // map
                            PbRoleSet_VnClass.make(roleSetAttribute, vnClassAttribute)

                            // roles
                            getXPaths(argmapElement, "./role")!!
                                .iteratorOfElements()
                                .asSequence()
                                .forEach {

                                    // pb attributes (arg)
                                    val argAttribute = it.getAttribute("pb-arg")

                                    // vn attributes (theta)
                                    val thetaAttribute = it.getAttribute("vn-theta")

                                    // propbank role
                                    val pbRole = PbRole.make(roleSetAttribute, argAttribute)

                                    // vn role
                                    val vnRole = make(vnClassAttribute, Theta.make(thetaAttribute))

                                    make(pbRole, vnRole)
                                }
                        }
                }
        }
    }
}
