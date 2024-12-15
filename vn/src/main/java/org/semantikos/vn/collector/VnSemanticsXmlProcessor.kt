package org.semantikos.vn.collector

import org.semantikos.common.Utils.camelCase
import org.semantikos.common.XmlProcessor
import org.w3c.dom.Element
import org.xml.sax.SAXException
import java.io.IOException
import java.util.regex.Pattern
import javax.xml.parsers.ParserConfigurationException

class VnSemanticsXmlProcessor : XmlProcessor() {

    @Throws(ParserConfigurationException::class, SAXException::class, IOException::class)
    override fun process(xml: String): String {
        val e = docFromString("<root>$xml</root>")
        return process(e)
    }

    companion object {

        private const val LOG_ONLY = false
        private const val TARGET_34 = true

        const val START_ARGS: Char = '('
        const val END_ARGS: Char = ')'
        const val SEP_ARGS: String = ", "
        const val SEP_FORMULA: String = "\n"

        const val EVENT_REGEX33: String = "E[0-1]?"
        const val EVENT_REGEX34: String = "[eëE][0-8]?"
        val EVENT_ASPECT_REGEX: String = "(during|end|result|start)\\(" + (if (TARGET_34) EVENT_REGEX34 else EVENT_REGEX33) + "\\)"

        const val SPECIFIC_REGEX33: String = "Direction|Emotion|Endstate|Form|Instrument|Light|Material|Motion|Odor|Patient|Pos|Prep|Prop|Role|Sound|Theme|Theme_i|Theme_j|Weather_type"
        const val SPECIFIC_REGEX34: String =
            "V_Attribute|V_Configuration|V_Destination|V_Direction|V_Emotion|V_Eventuality|V_Final_State|V_Form|V_Injury|V_Instrument|V_Light|V_Material|V_Manner|V_Movement|V_Odor|V_Orientation|V_Patient|V_Position|V_Spatial_Relation|V_Sound|V_State|V_Theme|V_Trajectory|V_Weather"

        const val CONSTANT_REGEX33: String = "ch_of_loc|ch_of_poss|ch_of_state|ch_on_scale|deceptive|directedmotion|forceful|hostile|illegal|playful|quality|toward|tr_of_info"
        const val CONSTANT_REGEX34: String =
            "abstract|agent|manner|directedmotion|fast|forceful|from|illegal|importance|movement|physical|physical/abstract|toward|ch_of_loc|ch_of_poss|ch_of_state|ch_on_scale|tr_of_info|stimulated|stimulating|enticed|enticing|deceptive|deceived|hostile|playful|unserious|quality|negative_emotion|slow"

        val EVENT_PATTERN: Pattern = Pattern.compile(if (TARGET_34) EVENT_REGEX34 else EVENT_REGEX33, Pattern.CASE_INSENSITIVE)
        val EVENT_ASPECT_PATTERN: Pattern = Pattern.compile(EVENT_ASPECT_REGEX, Pattern.CASE_INSENSITIVE)
        val SPECIFIC_PATTERN: Pattern = Pattern.compile(if (TARGET_34) SPECIFIC_REGEX34 else SPECIFIC_REGEX33, Pattern.CASE_INSENSITIVE)
        val CONSTANT_PATTERN: Pattern = Pattern.compile(if (TARGET_34) CONSTANT_REGEX34 else CONSTANT_REGEX33, Pattern.CASE_INSENSITIVE)

        @Throws(RuntimeException::class)
        private fun process(root: Element): String {
            root.normalize()

            val sb = StringBuilder()

            val preds = root.getElementsByTagName("PRED")
            val m = preds.length
            for (i in 0..<m) {
                val pred = preds.item(i) as Element

                val neg = pred.getAttribute("bool").trim { it <= ' ' }
                if (!neg.isEmpty()) {
                    sb.append(neg)
                }

                val value = pred.getAttribute("value").trim { it <= ' ' }
                sb.append(value)

                // args
                val argss = pred.getElementsByTagName("ARGS")
                if (argss.length > 1) {
                    throw RuntimeException("ARGS number at pred $i")
                }

                // args
                sb.append(START_ARGS)
                var a = 0
                val args = pred.getElementsByTagName("ARG")
                val n = args.length
                for (j in 0..<n) {
                    val arg = args.item(j) as Element
                    val t = arg.getAttribute("type").trim { it <= ' ' }
                    val v = arg.getAttribute("value").trim { it <= ' ' }

                    if (t.equals("ThemRole", ignoreCase = true)) {
                        if (a++ > 0) {
                            sb.append(SEP_ARGS)
                        }
                        sb.append(v)
                    }
                }
                for (j in 0..<n) {
                    val arg = args.item(j) as Element
                    val t = arg.getAttribute("type").trim { it <= ' ' }
                    var v = arg.getAttribute("value").trim { it <= ' ' }
                    if (!t.equals("ThemRole", ignoreCase = true)) {
                        if (a++ > 0) {
                            sb.append(SEP_ARGS)
                        }

                        // EVENT
                        if (t.equals("Event", ignoreCase = true)) {
                            val v2 = if (v[0] == '?') v.substring(1) else v
                            // System.out.println("@EVENT " + v);
                            if (EVENT_PATTERN.matcher(v2).matches()) {
                                v = v.uppercase()
                            } else if (EVENT_ASPECT_PATTERN.matcher(v2).matches()) {
                                v = camelCase(v)
                            } else {
                                if (LOG_ONLY) {
                                    System.err.println("SEMANTICS Event expression <$v>")
                                } else {
                                    throw RuntimeException("SEMANTICS Event expression <$v>")
                                }
                            }
                            sb.append("event:").append(v)
                        } else if (t.equals("VerbSpecific", ignoreCase = true)) {
                            val v2 = if (v[0] == '?') v.substring(1) else v
                            //System.out.println("@SPEC " + v);
                            if (!SPECIFIC_PATTERN.matcher(v2).matches()) {
                                if (LOG_ONLY) {
                                    System.err.println("SEMANTICS VerbSpecific expression <$v>")
                                } else {
                                    throw RuntimeException("SEMANTICS VerbSpecific expression <$v>")
                                }
                            }
                            sb.append("verbspecific:").append(v)
                        } else if (t.equals("Constant", ignoreCase = true)) {
                            //System.out.println("@CONST " + v);
                            if (!CONSTANT_PATTERN.matcher(v).matches()) {
                                if (LOG_ONLY) {
                                    System.err.println("SEMANTICS Constant expression <$v>")
                                } else {
                                    throw RuntimeException("SEMANTICS Constant expression <$v>")
                                }
                            }
                            sb.append("constant:").append(v)
                        } else if (t.equals("PredSpecific", ignoreCase = true)) {
                            // ignore
                        } else {
                            throw RuntimeException("type $t")
                        }
                    }
                }
                sb.append(END_ARGS)
                if (i < m - 1) {
                    sb.append(SEP_FORMULA)
                }
            }
            return sb.toString()
        }
    }
}
