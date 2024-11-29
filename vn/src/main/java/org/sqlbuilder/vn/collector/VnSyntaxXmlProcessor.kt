package org.sqlbuilder.vn.collector

import org.sqlbuilder.common.XmlChecker
import org.sqlbuilder.common.XmlChecker.checkAttributeName
import org.sqlbuilder.common.XmlChecker.checkAttributeValue
import org.sqlbuilder.common.XmlChecker.checkElementName
import org.sqlbuilder.common.XmlProcessor
import org.w3c.dom.Element
import org.xml.sax.SAXException
import java.io.IOException
import java.util.regex.Pattern
import javax.xml.parsers.ParserConfigurationException

class VnSyntaxXmlProcessor : XmlProcessor() {

    @Throws(ParserConfigurationException::class, SAXException::class, IOException::class)
    override fun process(xml: String): String {
        val e = docFromString("<root>$xml</root>")
        return process(e)
    }

    companion object {

        private const val LOG_ONLY = false
        private const val TARGET_34 = true

        const val SEP_CAT: String = "\n"
        const val START_SELRESTR: String = " <"
        const val END_SELRESTR: Char = '>'
        const val START_SYNRESTR: String = " {"
        const val END_SYNRESTR: Char = '}'

        const val CATS: String = "ADJ|ADV|LEX|NP|PREP|VERB|VP"

        private const val NP_REGEX33 =
            "Affector|Agent|Asset|Attribute|Axis|Beneficiary|Causer|Co-Agent|Context|Co-Patient|Co-Theme|Destination|Duration|Experiencer|Extent|Goal|Initial_Location|Initial_State|Instrument|Location|Manner|Material|Path|Patient|Pivot|Precondition|Predicate|Product|Recipient|Reflexive|Result|Source|Stimulus|Theme|Time|Topic|Trajectory|Value"
        private const val NP_REGEX34 =
            "Affector|Agent|Asset|Attribute|Axis|Beneficiary|Causer|Circumstance|Co-Agent|Co-Patient|Co-Theme|Destination|Duration|Eventuality|Experiencer|Extent|Goal|Initial_Location|Initial_State|Instrument|Location|Maleficiary|Manner|Material|Path|Patient|Pivot|Precondition|Product|Recipient|Reflexive|Result|Source|Stimulus|Subeventuality|Theme|Topic|Trajectory|Value"

        private const val PREP_REGEX33 = "about|after|against|among|as|as_if|as_though|at|before|between|by|concerning|for|from|in|in_between|into|like|of|off|on|onto|out_of|over|regarding|respecting|through|to|towards|under|until|upon|with"
        private const val PREP_REGEX34 = "about|after|against|among|as|as_if|as_though|at|before|between|by|concerning|for|from|in|in_between|into|like|of|off|on|onto|out_of|over|regarding|respecting|through|to|toward|towards|under|upon|with"

        private const val LEX_REGEX33 = "and|apart|as|at|away|be|down|it|like|of|out|'s|there|to|to be|together|up|\\[\\+be\\]|it\\[\\+be\\]"
        private const val LEX_REGEX34 = "and|apart|as|at|be|down|it|like|of|out|'s|there|to|to be|together|up|\\[\\+be\\]|it\\[\\+be\\]"

        private const val ADV_REGEX33 = "Trajectory"
        private const val ADV_REGEX34 = "Manner|Path|Trajectory"

        private const val VP_REGEX33 = ".*"
        private const val VP_REGEX34 = "Eventuality"

        const val SELRESTR: String = "at|body_part|concrete|dest|dest_conf|dest_dir|dir|gol|loc|location|path|refl|region|spatial|src|src_conf|state"
        const val SYNRESTR: String =
            "ac_ing|ac_to_inf|adv_loc|be_sc_ing|definite|for_comp|genitive|how_extract|np_ing|np_omit_ing|np_p_ing|np_ppart|np_to_inf|oc_bare_inf|oc_ing|oc_to_inf|plural|poss_ing|quotation|refl|rs_to_inf|sc_ing|sc_to_inf|sentential|small_clause|tensed_that|that_comp|to_be|what_extract|what_inf|wh_comp|wheth_inf|wh_extract|wh_inf|wh_ing"

        val NP_PATTERN: Pattern = Pattern.compile(if (TARGET_34) NP_REGEX34 else NP_REGEX33, Pattern.CASE_INSENSITIVE)
        val PREP_PATTERN: Pattern = Pattern.compile(if (TARGET_34) PREP_REGEX34 else PREP_REGEX33, Pattern.CASE_INSENSITIVE)
        val LEX_PATTERN: Pattern = Pattern.compile(if (TARGET_34) LEX_REGEX34 else LEX_REGEX33, Pattern.CASE_INSENSITIVE)
        val ADV_PATTERN: Pattern = Pattern.compile(if (TARGET_34) ADV_REGEX34 else ADV_REGEX33, Pattern.CASE_INSENSITIVE)
        val VP_PATTERN: Pattern = Pattern.compile(if (TARGET_34) VP_REGEX34 else VP_REGEX33, Pattern.CASE_INSENSITIVE)

        private fun getAttrs(attrs: String): Array<String> {
            val containsBar = attrs.contains("|")
            return attrs
                .split((if (containsBar) " *\\| *" else "\\s+").toRegex())
                .dropLastWhile { it.isEmpty() }
                .map { it.replace(' ', '_') }
                .toTypedArray<String>()
        }

        @Throws(RuntimeException::class)
        private fun process(root: Element): String {
            root.normalize()

            val sb = StringBuilder()

            // CAT
            val cats = root.childNodes
            val m = cats.length
            for (i in 0..<m) {
                val node = cats.item(i)
                if (node !is Element) {
                    // TODO Checker.checkEmpty(node, "root", LOG_ONLY)
                    continue
                }
                val e = node

                // name
                val name = e.nodeName
                checkElementName(name, CATS, "SYNTAX $name element found, expected $CATS")
                sb.append(name)

                // attribute check
                checkAttributeName(e, "value", "SYNTAX $name has no 'value' attr", LOG_ONLY)

                // 'value' attribute value
                var value = e.getAttribute("value").trim { it <= ' ' }
                if (!value.isEmpty()) {
                    when (name) {
                        "PREP"        -> {
                            val values = getAttrs(value.trim { it <= ' ' })
                            val values2 = values.map { s: String? -> if (s!![0] == '?') s.substring(1) else s }.toTypedArray()
                            XmlChecker.checkAttributeValues(values2, PREP_PATTERN, "SYNTAX $name has unexpected 'value' attr: $value", LOG_ONLY)
                            value = values.joinToString(separator = " | ")
                        }

                        "NP"          -> {
                            value = value.trim { it <= ' ' }
                            checkAttributeValue(if (value[0] == '?') value.substring(1) else value, NP_PATTERN, "SYNTAX $name has unexpected 'value' attr: $value", LOG_ONLY)
                        }

                        "LEX"         -> {
                            checkAttributeValue(value.trim { it <= ' ' }, LEX_PATTERN, "SYNTAX $name has unexpected 'value' attr: $value", LOG_ONLY)
                        }

                        "ADV"         -> {
                            checkAttributeValue(value.trim { it <= ' ' }, ADV_PATTERN, "SYNTAX $name has unexpected 'value' attr: $value", LOG_ONLY)
                        }

                        "VP"          -> {
                            checkAttributeValue(value.trim { it <= ' ' }, VP_PATTERN, "SYNTAX $name has unexpected 'value' attr: $value", LOG_ONLY)
                        }

                        "VERB", "ADJ" -> {
                            println("$name has 'value' attr: $value")
                        }

                        else          -> {
                            println("$name has 'value' attr: $value")
                        }
                    }

                    // collect value
                    sb.append(' ')
                    sb.append(value)
                }

                // SELRESTRS|SYNRESTRS

                val nodes2 = node.childNodes
                for (j in 0..<nodes2.length) {
                    val node2 = nodes2.item(j)
                    if (node2 !is Element) {
                        // TODO XmlChecker.checkEmpty(node2, "SYNTAX found non element as child of " + name, LOG_ONLY)
                        continue
                    }
                    val e2 = node2

                    // name
                    val name2 = e2.nodeName
                    checkElementName(name2, "SELRESTRS|SYNRESTRS", "SYNTAX SELRESTRS|SYNRESTRS expected, found element: $name2")

                    // sb.append(" = ");
                    // sb.append(name2);

                    // attribute check
                    checkAttributeName(e2, "logic", "SYNTAX $name has no 'logic' attr", LOG_ONLY)

                    // 'Value' attribute value
                    var logic = e2.getAttribute("logic").trim { it <= ' ' }
                    if (!logic.isEmpty()) {
                        checkAttributeValue(logic, "or|and", "SYNTAX $name has unexpected 'logic' attr: $logic", LOG_ONLY)
                    }

                    // SELRESTR|SYNRESTR

                    val nodes3 = node2.childNodes
                    for (k in 0..<nodes3.length) {
                        val node3 = nodes3.item(k)
                        if (node3 !is Element) {
                            //TODO XmlChecker.checkEmpty(node3, "SYNTAX " + "found non element as child of " + name2, LOG_ONLY)
                            continue
                        }
                        val e3 = node3

                        // name
                        val name3 = e3.nodeName
                        val isSynRestr = name3 == "SYNRESTR"
                        val isSelRestr = name3 == "SELRESTR"

                        checkElementName(name3, "SELRESTR|SYNRESTR", "SYNTAX SELRESTR|SYNRESTR expected, found element: $name3")

                        // sb.append(" - ");
                        // sb.append(name3);

                        // attribute check
                        checkAttributeName(e3, "Value|type", "SYNTAX $name3 has no 'Value|type' attr", LOG_ONLY)

                        // 'Value' attribute value
                        val value3 = e3.getAttribute("Value").trim { it <= ' ' }
                        if (!value3.isEmpty()) {
                            checkAttributeValue(value3, "(\\+|\\-)", "SYNTAX $name3 has unexpected 'Value' attr: $value3", LOG_ONLY)
                        }

                        // 'type' attribute value
                        val type3 = e3.getAttribute("type").trim { it <= ' ' }
                        if (!type3.isEmpty()) {
                            if (isSynRestr) {
                                checkAttributeValue(type3, SYNRESTR, "SYNTAX $name3 has unexpected synrestr 'Value' attr: $type3", LOG_ONLY)
                                // println("@SYN " + type3)
                            }
                            if (isSelRestr) {
                                checkAttributeValue(type3, SELRESTR, "SYNTAX $name3 has unexpected selrestr 'Value' attr: $type3", LOG_ONLY)
                                // println("@SEL " + type3)
                            }
                        }

                        if (!value3.isEmpty() || !type3.isEmpty()) {
                            sb.append(if (isSynRestr) START_SYNRESTR else START_SELRESTR)
                            sb.append(value3)
                            sb.append(type3)
                            sb.append(if (isSynRestr) END_SYNRESTR else END_SELRESTR)
                        }

                        if (logic.isEmpty()) {
                            logic = "and"
                        }
                        if (k < nodes3.length - 1) {
                            sb.append(' ')
                            sb.append(logic)
                        }
                    }
                }
                if (i < m - 1) {
                    sb.append(SEP_CAT)
                }
            }
            return sb.toString()
        }
    }
}
