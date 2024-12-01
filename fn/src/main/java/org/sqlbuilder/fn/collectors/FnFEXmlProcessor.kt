package org.sqlbuilder.fn.collectors

import org.sqlbuilder.common.XmlChecker.checkAttributeName
import org.sqlbuilder.common.XmlChecker.checkElementName
import org.sqlbuilder.common.XmlChecker.checkSubElements
import org.sqlbuilder.common.XmlProcessor
import org.w3c.dom.Element
import org.xml.sax.SAXException
import java.io.IOException
import javax.xml.parsers.ParserConfigurationException

class FnFEXmlProcessor : XmlProcessor() {

    @Throws(ParserConfigurationException::class, SAXException::class, IOException::class)
    override fun process(xml: String): String {
        val e = docFromString("<root>$xml</root>")
        return process(e)
    }

    companion object {

        private const val LOG_ONLY = false

        @Throws(RuntimeException::class)
        private fun process(root: Element): String {
            root.normalize()

            val sb = StringBuilder()
            root.getElementsByTagName("def-root")
                .iterator()
                .forEach { e ->
                    e.childNodes
                        .iterator()
                        .forEach {
                            if (it !is Element) {
                                var value = it.nodeValue
                                value = value.replace("\n\n+".toRegex(), "\n")
                                sb.append(value)
                            } else {
                                val e2 = it
                                var name2 = it.nodeName

                                // fix
                                if ("fex" == name2) {
                                    e2.ownerDocument.renameNode(it, null, "fen")
                                    e2.removeAttribute("name")
                                    name2 = it.nodeName
                                } else if ("m" == name2) {
                                    e2.ownerDocument.renameNode(it, null, "em")
                                    name2 = it.nodeName
                                }

                                // transform
                                checkElementName(name2, "(fen|t|ment|ex|em)", "top")
                                when (name2) {
                                    "ex"         -> {
                                        checkAttributeName(e2, null, "ex", LOG_ONLY)

                                        val example: String = processExample(e2)
                                        sb.append("<ex>").append(example).append("</ex>")
                                    }

                                    "fen"        -> {
                                        checkSubElements(e2, null, null, LOG_ONLY)
                                        checkAttributeName(e2, null, null, LOG_ONLY)

                                        val value = e2.textContent.replace("\n\n+".toRegex(), "\n")
                                        sb.append("<fen>").append(value).append("</fen>")
                                    }

                                    "t"          -> {
                                        checkSubElements(e2, null, null, LOG_ONLY)
                                        checkAttributeName(e2, null, null, LOG_ONLY)

                                        val value = e2.textContent.replace("\n\n+".toRegex(), "\n")
                                        sb.append("<t>").append(value).append("</t>")
                                    }

                                    "ment", "em" -> {
                                        checkSubElements(e2, null, null, LOG_ONLY)
                                        checkAttributeName(e2, null, null, LOG_ONLY)

                                        val value = e2.textContent.replace("\n\n+".toRegex(), "\n")
                                        sb.append(value)
                                    }
                                }
                            }
                        }
                }
            return sb.toString() 
                .replace("\\s\\s+".toRegex(), " ") // .replaceAll("\n\n+", "\n")
                .replace("\n+".toRegex(), "")
        }

        @Throws(RuntimeException::class)
        private fun processExample(root: Element): String {
            root.normalize()

            val sb = StringBuilder()
            root.childNodes
                .iterator()
                .forEach {
                    if (it !is Element) {
                        var value = it.nodeValue
                        value = value.replace("\n\n+".toRegex(), "\n")
                        sb.append(value)
                    } else {
                        val e2 = it
                        val name2 = it.nodeName

                        // transform
                        checkElementName(name2, "(fex|t|ment|gov|m|target)", name2)
                        when (name2) {
                            "fex" -> {
                                // TODO 82 errors
                                // XmlChecker.check_SubElements(e2, null, null)
                                checkSubElements(e2, "(t)", null, LOG_ONLY)
                                checkAttributeName(e2, "(name)", null, LOG_ONLY)
                                val attr = e2.getAttribute("name")

                                // XmlChecker.check_ElementAttributeValue(attr,"(act|Actor|Address|Addressee|Affected_party|Age|Aggregate|Alterant|Area|ass|Assessor|Audience|Bad_entity|Basis|bed|beh|Benefited_party|Body_location|Category|Cause|Change_agent|Characteristic|Charges|Chosen|Chrg|ci|cir|Circumstances|clo|Clothing|Code|Cognizer_1|col|com|Commitment|Communicator|Completeness|Concept_1|Condition|Connections|Consideration|Containing_object|Contents|Contrast_set|Cr|CrEnt|Criterion|Crop|dan|dan_ent|Danger|Deceased|Decision|DefEvnt|dep|depe|Depictive|deps|Desirable_action|Desirable_situation|Determinant|Deviation|Dimension|dir|dis|Distance|Documents|dom|Domain|Dryee|dur|dut|Duty|Earnings|eff|Ego|el|Emotion|Enabled_action|Enabled_situation|Entry_path|est|eva|Eval|Evaluation|Eventuality|Evt|exe|Existing_member|exp|Factor|Father|fen|Final_value|finc|Fine|Firearm|fix|Flammables|Foc|Focal_occasion|Focal_participant|Food|Force|Frequency|fun|Game|goa|Goal|Goods|gov|Grantee|gro|Hair|Harmful_event|Hidden_object|Hindrance|Holding_Location|hos|Id|Idea|Impactee|Inc|Incident|ind|ind2|inis|Initial_element|Initial_state|Injury|Inst|Insulator|Intended_event|Intended_referent|Interlocutor_2|Intermediary|iss|Issue|I_state|I-state|ite|Item-2|Knot|lab|Landmark|Landmark_event|Liquid|loc|Main_statement|man|Manner|mea|Means|Measurement|Medium|Member|men|Message|mis|mns|Msg|nam|Name_source|neg|New_duration|New_idea|New_leader|Number_of_possibilities|Obligation|Obstruction|Occupant|Official|old|Old|oo|opi|Original|Other|par|Part_2|Partner_2|Part_prop|Party_2|Pat|Path|Patient|Period|Phen|Phenomenon_2|Pieces|pl|pla|Plan|Posit|Position|Possession|Prize|Process|Production|Property|Proposition|pur|que|Rank|rat|Represented|Requirement|Resistant_surface|Resource|Response|Responsible_party|Resulting_action|Roadway|rol|Role|Rope|Rule|ser|Sev|Severity|Shine|sig|Simulated_entity|Sit|Size|Size_change|Skill|soc|Society|Sought_entity|Sound_maker|Source|Source_representation|Spd|Speed|src|Standard_item|State|stu|Stuff|Subject|Subset|Sub_source|Supporter|System|Target|Target_location|Task|tem|Temperature_goal|Term2|tex|the|Theme|thm1|ti|Time|toc|top|tos|Total|toxs|Traveller|Typ|Undesirable_location|User|val|Value|Variable|Weapon|Witness)")
                                val value = e2.textContent.replace("\n\n+".toRegex(), "\n")
                                sb.append("<fex name='").append(attr).append("'>").append(value).append("</fex>")
                            }

                            "t"   -> {
                                // TODO 59 errors
                                // XmlChecker.check_SubElements(e2, null, null)
                                checkSubElements(e2, "(fex)", null, LOG_ONLY)
                                checkAttributeName(e2, null, null, LOG_ONLY)

                                val value = e2.textContent.replace("\n\n+".toRegex(), "\n")
                                sb.append('<').append(name2).append('>').append(value).append("</").append(name2).append('>')
                            }

                            "m"   -> {
                                checkSubElements(e2, null, null, LOG_ONLY)
                                // TODO 5 errors
                                // XmlChecker.check_AttributeName(e2, null, null)
                                checkAttributeName(e2, "(name)", null, LOG_ONLY)

                                val value = e2.textContent.replace("\n\n+".toRegex(), "\n")
                                sb.append('<').append(name2).append('>').append(value).append("</").append(name2).append('>')
                            }

                            else  -> {
                                checkSubElements(e2, null, null, LOG_ONLY)
                                checkAttributeName(e2, null, null, LOG_ONLY)

                                val value = e2.textContent.replace("\n\n+".toRegex(), "\n")
                                sb.append('<').append(name2).append('>').append(value).append("</").append(name2).append('>')
                            }
                        }
                    }
                }
            return sb.toString()
        }
    }
}
