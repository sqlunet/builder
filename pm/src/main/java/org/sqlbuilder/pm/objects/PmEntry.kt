package org.sqlbuilder.pm.objects

import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.ParseException
import org.sqlbuilder.common.Utils.nullable

class PmEntry : Insertable {

    private var role: PmRole? = null

    // wordnet
    @JvmField
    var word: String? = null

    @JvmField
    var sensekey: String? = null

    // verbnet
    @JvmField
    val vn: VnRoleAlias = VnRoleAlias()

    // propbank
    @JvmField
    val pb: PbRoleAlias = PbRoleAlias()

    // framenet
    @JvmField
    val fn: FnRoleAlias = FnRoleAlias()

    // sources
    private var sources: Long = 0

    override fun dataRow(): String {
        return "${role!!.intId},'$word','${nullable(sensekey)}',${vn.dataRow()},${pb.dataRow()},${fn.dataRow()},$sources"
    }

    override fun comment(): String {
        return "PM[${role!!.intId}], WN['${word}','${nullable(sensekey)}'], VN[${vn.dataRow()}], PB[${pb.dataRow()}], FN[${fn.dataRow()}], SRC[$sources]"
        return "${role!!.predicate},${role!!.role},${role!!.pos}"
    }

    companion object {

        //	const val ID_LANG = 0 // this column contains the language of the predicate. id:eng
        const val ID_POS: Int = 1 // this columnn contains the part-of-speech of the predicate. id:n
        const val ID_PRED: Int = 2 // this column contains the predicate. id:abatement.01
        const val ID_ROLE: Int = 3 // this column contains the role. id:0

        const val VN_CLASS: Int = 4 // this column contains the information of the VerbNet class. vn:withdraw-82
        //	const val VN_CLASS_NUMBER = 5 // this column contains the information of the VerbNet class number. vn:82
        const val VN_SUBCLASS: Int = 6 // this column contains the information of VerbNet subclass. vn:withdraw-82-1
        //	const val VN_SUBCLASS_NUMBER = 7 // this column contains the information of the VerbNet subclass number. vn:82-1
        const val VN_LEMMA: Int = 8 // this column contains the information of the verb lemma. vn:pull_back
        const val VN_ROLE: Int = 9 // this column contains the information of the VerbNet thematic-role. vn:Source
        const val WN_SENSE: Int = 10 // this column contains the information of the word sense in WordNet. wn:pull_back%2:32:12

        //	const val MCR_ILI_OFFSET = 11 // this column contains the information of the ILI number in the MCR3.0. mcr:ili-30-00799383-v
        const val FN_FRAME: Int = 12 // this column contains the information of the frame in FrameNet. fn:Going_back_on_a_commitment
        const val FN_LE: Int = 13 // this column contains the information of the corresponding lexical-entry in FrameNet. fn:NULL
        const val FN_FRAME_ELEMENT: Int = 14 // this column contains the information of the frame-element in FrameNet. fn:NULL

        const val PB_ROLESET: Int = 15 // this column contains the information of the predicate in PropBank. pb:NULL
        const val PB_ARG: Int = 16 // this column contains the information of the predicate argument in PropBank. pb:NULL

        // const val MCR_BC = 17 // this column contains the information if the verb sense it is Base Concept or not in the MCR3.0. mcr:1
        // const val MCR_DOMAIN = 18 // this column contains the information of the WordNet domain aligned to WordNet 3.0 in the MCR3.0. mcr:factotum
        // const val MCR_SUMO = 19 // this column contains the information of the AdimenSUMO in the MCR3.0. mcr:Communication
        // const val MCR_TO = 20 // this column contains the information of the MCR Top Ontology in the MCR3.0. mcr:Dynamic;communication;
        // const val MCR_LEXNAME = 21 // this column contains the information of the MCR Lexicographical file name. mcr:communication
        // const val MCR_BLC = 22 // this column contains the information of the Base Level Concept of the WordNet verb sense in the MCR3.0.mcr:back_away%2:32:00
        // const val WN_SENSEFREC = 23 // this column contains the information of the frequency of the WordNet 3.0 verb sense. wn:0
        // const val WN_SYNSET_REL_NUM = 24 // this column contains the information of the number of relations of the WordNet 3.0 verb sense. wn:004
        // const val ESO_CLASS = 25 // this column contains the information of the class of the ESO ontology.
        // const val ESO_ROLE = 26 // this column contains the information of the role of the ESO ontology.
        const val SOURCE: Int = 27 // this column contains the information of how the row has been obtained. SEMLINK;FRAME;SYNONYMS

        // sources
        const val SEMLINK: Int = 0x1
        const val SYNONYMS: Int = 0x2
        const val FRAME: Int = 0x10
        const val FN_FE: Int = 0x20
        const val ADDED_FRAME_FN_ROLE: Int = 0x40 // ADDED_FRAME-FN_ROLE
        const val FN_MAPPING: Int = 0x100
        const val VN_MAPPING: Int = 0x200
        const val PREDICATE_MAPPING: Int = 0x400
        const val ROLE_MAPPING: Int = 0x800
        const val WN_MISSING: Int = 0x1000

        @JvmStatic
        @Throws(ParseException::class)
        fun parse(line: String): PmEntry {
            // split into fields
            val columns = line.split("\t".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (columns.size > SOURCE + 1) {
                throw ParseException("Line has more fields than expected")
            }

            val entry = PmEntry()

            // predicate, role, pos
            entry.role = PmRole.parse(columns)

            // lemma
            val lemma = columns[VN_LEMMA].trim { it <= ' ' }
            entry.word = lemma.substring(3)

            // source
            val prefix = lemma.substring(0, 2)
            if ("vn" != prefix && "fn" != prefix) {
                throw ParseException(prefix)
            }
            if ("NULL" == entry.word) {
                entry.word = entry.role!!.predicate.word
            }

            // sensekey
            var sensekey = columns[WN_SENSE].trim { it <= ' ' }.substring(3) // .replace('_', ' ')
            if (!sensekey.isEmpty() && "NULL" != sensekey) {
                if (sensekey.startsWith("?")) {
                    sensekey = sensekey.substring(1)
                }
            } else if (sensekey.isEmpty()) {
                throw ParseException("Empty sensekey for $line")
            }
            entry.sensekey = if ("NULL".equals(sensekey, ignoreCase = true)) null else "$sensekey::"

            // verbnet
            val vnsubclass = if ("vn:NULL" == columns[VN_SUBCLASS]) null else columns[VN_SUBCLASS].trim { it <= ' ' }.substring(3)
            entry.vn.clazz = vnsubclass ?: if ("vn:NULL" == columns[VN_CLASS]) null else columns[VN_CLASS].trim { it <= ' ' }.substring(3)
            entry.vn.role = if ("vn:NULL" == columns[VN_ROLE]) null else columns[VN_ROLE].trim { it <= ' ' }.substring(3)

            // propbank
            entry.pb.roleset = if ("pb:NULL" == columns[PB_ROLESET]) null else columns[PB_ROLESET].trim { it <= ' ' }.substring(3)
            entry.pb.arg = if ("pb:NULL" == columns[PB_ARG]) null else columns[PB_ARG].trim { it <= ' ' }.substring(3)

            // framenet
            entry.fn.frame = if ("fn:NULL" == columns[FN_FRAME]) null else columns[FN_FRAME].trim { it <= ' ' }.substring(3)
            entry.fn.fetype = if ("fn:NULL" == columns[FN_FRAME_ELEMENT]) null else columns[FN_FRAME_ELEMENT].trim { it <= ' ' }.substring(3)
            entry.fn.lu = if ("fn:NULL" == columns[FN_LE]) null else columns[FN_LE].trim { it <= ' ' }.substring(3)

            // source
            entry.sources = 0
            if (columns.size > SOURCE) {
                for (source in columns[SOURCE].split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                    if ("SEMLINK" == source) {
                        entry.sources = entry.sources or SEMLINK.toLong()
                    }
                    if ("SYNONYMS" == source) {
                        entry.sources = entry.sources or SYNONYMS.toLong()
                    }
                    if ("FRAME" == source) {
                        entry.sources = entry.sources or FRAME.toLong()
                    }
                    if ("FN_FE" == source) {
                        entry.sources = entry.sources or FN_FE.toLong()
                    }
                    if ("ADDED_FRAME-FN_ROLE" == source) {
                        entry.sources = entry.sources or ADDED_FRAME_FN_ROLE.toLong()
                    }
                    if ("FN_MAPPING" == source) {
                        entry.sources = entry.sources or FN_MAPPING.toLong()
                    }
                    if ("VN_MAPPING" == source) {
                        entry.sources = entry.sources or VN_MAPPING.toLong()
                    }
                    if ("PREDICATE_MAPPING" == source) {
                        entry.sources = entry.sources or PREDICATE_MAPPING.toLong()
                    }
                    if ("ROLE_MAPPING" == source) {
                        entry.sources = entry.sources or ROLE_MAPPING.toLong()
                    }
                    if ("WN_MISSING" == source) {
                        entry.sources = entry.sources or WN_MISSING.toLong()
                    }
                }
            }
            return entry
        }
    }
}
