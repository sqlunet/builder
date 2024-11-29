package org.sqlbuilder.vn.collector

import org.sqlbuilder.common.XmlChecker.checkAttributeName
import org.sqlbuilder.common.XmlChecker.checkAttributeValue
import org.sqlbuilder.common.XmlChecker.checkElementName
import org.sqlbuilder.common.XmlChecker.checkEmpty
import org.sqlbuilder.common.XmlProcessor
import org.w3c.dom.Element
import org.xml.sax.SAXException
import java.io.IOException
import java.util.regex.Pattern
import javax.xml.parsers.ParserConfigurationException

class VnRestrsXmlProcessor : XmlProcessor() {

    @Throws(ParserConfigurationException::class, SAXException::class, IOException::class)
    override fun process(xml: String): String {
        val e = docFromString("<root>$xml</root>")
        return process(e)
    }

    companion object {

        private const val LOG_ONLY = false
        private const val TARGET_34 = true

        const val START_SELRESTR: Char = '<'
        const val END_SELRESTR: Char = '>'
        const val START_SYNRESTR: Char = '{'
        const val END_SYNRESTR: Char = '}'

        const val RESTRS: String = "SELRESTRS|SYNRESTRS"

        const val RESTRS2: String = "SELRESTR|SYNRESTR|SELRESTRS"

        const val SELRESTR_REGEX33: String =
            "abstract|animal|animate|biotic|body_part|comestible|communication|concrete|currency|dest|dest_conf|dest_dir|dir|elongated|eventive|force|garment|human|int_control|loc|location|machine|nonrigid|organization|path|plural|pointy|refl|region|solid|sound|spatial|src|state|substance|time|vehicle"
        const val SELRESTR_REGEX34: String =
            "abstract|animal|animate|at|biotic|body_part|comestible|communication|concrete|currency|dest|dest_conf|dest_dir|dir|elongated|eventive|force|garment|gol|human|int_control|loc|location|machine|nonrigid|organization|path|plural|pointy|question|refl|region|solid|sound|spatial|src|src_conf|state|substance|vehicle|vehicle_part"

        const val SYNRESTR_REGEX33: String =
            "ac_ing|ac_to_inf|adv_loc|be_sc_ing|definite|for_comp|genitive|how_extract|np_ing|np_omit_ing|np_p_ing|np_ppart|np_to_inf|oc_bare_inf|oc_ing|oc_to_inf|plural|poss_ing|quotation|refl|rs_to_inf|sc_ing|sc_to_inf|sentential|small_clause|tensed_that|that_comp|to_be|what_extract|what_inf|wh_comp|wheth_inf|wh_extract|wh_inf|wh_ing"
        const val SYNRESTR_REGEX34: String =
            "ac_ing|ac_to_inf|adv_loc|be_sc_ing|definite|for_comp|genitive|how_extract|np_ing|np_omit_ing|np_p_ing|np_ppart|np_to_inf|oc_bare_inf|oc_ing|oc_to_inf|plural|poss_ing|quotation|refl|rs_to_inf|sc_ing|sc_to_inf|sentential|small_clause|tensed_that|that_comp|to_be|what_extract|what_inf|wh_comp|wheth_inf|wh_extract|wh_inf|wh_ing"

        val SELRESTR_PATTERN: Pattern = Pattern.compile(if (TARGET_34) SELRESTR_REGEX34 else SELRESTR_REGEX33, Pattern.CASE_INSENSITIVE)
        val SYNRESTR_PATTERN: Pattern = Pattern.compile(if (TARGET_34) SYNRESTR_REGEX34 else SYNRESTR_REGEX33, Pattern.CASE_INSENSITIVE)

        @Throws(RuntimeException::class)
        private fun process(root: Element): String {
            root.normalize()

            val sb = StringBuilder()

            // SELRESTRS|SYNRESTRS
            val nodes2 = root.childNodes
            for (j in 0..<nodes2.length) {
                val node2 = nodes2.item(j)
                if (node2 !is Element) {
                    checkEmpty(node2, "RESTR: non-empty non-element at root", LOG_ONLY)
                    continue
                }
                val e2 = node2
                val restrs: String = process2(e2)
                sb.append(restrs)
            }

            // if (i < m - 1)
            // sb.append(VnRestrsXmlProcessor.SEP_CAT)
            return sb.toString()
        }

        @Throws(RuntimeException::class)
        private fun process2(e2: Element): String {
            e2.normalize()

            val sb = StringBuilder()

            // SELRESTRS|SYNRESTRS

            // name
            val name2 = e2.nodeName
            checkElementName(name2, RESTRS, "RESTR: $name2 found, expected: $RESTRS")

            // sb.append(" = ")
            // sb.append(name2)

            // attribute check
            checkAttributeName(e2, "logic", "RESTR: $name2 has no 'logic' attribute", LOG_ONLY)

            // 'Value' attribute value
            var logic = e2.getAttribute("logic").trim { it <= ' ' }
            if (!logic.isEmpty()) {
                checkAttributeValue(logic, "or|and", "RESTR: $name2 has 'logic' attribute value outside or|and", LOG_ONLY)
            }

            // SELRESTR|SYNRESTR

            val nodes3 = e2.childNodes
            for (k in 0..<nodes3.length) {
                val node3 = nodes3.item(k)
                if (node3 !is Element) {
                    // XmlChecker.checkEmpty(node3, "RESTR: $name2 has non-empty non-element $node3", LOG_ONLY)
                    continue
                }
                val e3 = node3

                // name
                val name3 = e3.nodeName
                val isSynRestr = name3 == "SYNRESTR"
                val isSelRestr = name3 == "SELRESTR"

                checkElementName(name3, RESTRS2, "RESTR: $name2 has unexpected element $name3, expected: $RESTRS2")

                // sb.append(" - ")
                // sb.append(name3)
                if ("SELRESTRS" == name3) {
                    val sub: String = process2(e3)
                    sb.append("( ")
                    sb.append(sub)
                    sb.append(" )")
                } else {
                    // attribute check
                    checkAttributeName(e3, "Value|type", "RESTR: $name3 has no 'Value'|'type' attribute", LOG_ONLY)

                    // 'Value' attribute value
                    val value3 = e3.getAttribute("Value").trim { it <= ' ' }
                    if (!value3.isEmpty()) {
                        checkAttributeValue(value3, "(\\+|\\-)", "RESTR: $name3 has 'value' not in (+/-)", LOG_ONLY)
                    }

                    // 'type' attribute value
                    val type3 = e3.getAttribute("type").trim { it <= ' ' }
                    if (!type3.isEmpty()) {
                        if (isSynRestr) {
                            checkAttributeValue(type3, SYNRESTR_PATTERN, "SYNRESTR: $name3 has no 'type' in $name3, expected: ${SYNRESTR_PATTERN.pattern()}", LOG_ONLY)
                        }
                        if (isSelRestr) {
                            checkAttributeValue(type3, SELRESTR_PATTERN, "SELRESTR: $name3 has no 'type' in $name3, expected: ${SELRESTR_PATTERN.pattern()}", LOG_ONLY)
                        }
                    }

                    if (!value3.isEmpty() || !type3.isEmpty()) {
                        sb.append(if (isSynRestr) START_SYNRESTR else START_SELRESTR)
                        sb.append(value3)
                        sb.append(type3)
                        sb.append(if (isSynRestr) END_SYNRESTR else END_SELRESTR)
                    }
                }

                if (logic.isEmpty()) {
                    logic = "and"
                }
                if (k < nodes3.length - 1) {
                    sb.append(' ')
                    sb.append(logic)
                    sb.append(' ')
                }
            }
            return sb.toString()
        }
    }
}
