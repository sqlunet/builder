package org.sqlbuilder.vn.objects

import org.sqlbuilder.vn.objects.Sensekey.Companion.parse

class Member
private constructor(
    @JvmField val lemma: String,
    @JvmField val senseKeys: List<Sensekey>?,
    @JvmField val groupings: List<Grouping>?,
    private val isDefinite: Boolean,
) {

    val quality: Float
        get() = if (this.isDefinite) 1f else .5f

    companion object {

        @JvmStatic
        fun make(wnword: String, wnSenses: String?, wnGrouping: String?): Member {
            val isDefiniteFlag = wnword.startsWith("?")
            val word: String = makeWord(wnword)
            val senseKeys: List<Sensekey>? = makeSensekeys(wnSenses)
            val groupings: List<Grouping>? = makeGroupings(wnGrouping)

            return Member(word, senseKeys, groupings, isDefiniteFlag)
        }

        @JvmStatic
        fun makeWord(wnword: String): String {
            // word
            var word = wnword
            if (word.startsWith("?")) {
                word = word.substring(1)
            }
            return word.replace('_', ' ')
        }

        @JvmStatic
        fun makeSensekeys(wnSenses: String?): List<Sensekey>? {
            // senses
            var senseKeys: MutableList<Sensekey>? = null
            if (wnSenses != null && !wnSenses.trim { it <= ' ' }.isEmpty()) {
                var wnSenses2 = wnSenses.trim { it <= ' ' }
                if (wnSenses2.indexOf('\n') != -1) {
                    wnSenses2 = wnSenses2.replace("\n", "")
                }
                if (wnSenses2.indexOf('\r') != -1) {
                    wnSenses2 = wnSenses2.replace("\r", "")
                }
                val senseKeyNames = wnSenses2.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (senseKeyName in senseKeyNames) {
                    // get sensekey
                    val sensekey = parse(senseKeyName)
                    if (senseKeys == null) {
                        senseKeys = ArrayList<Sensekey>()
                    }
                    senseKeys.add(sensekey)
                }
            }
            return senseKeys
        }

        fun makeGroupings(groupingAttribute: String?): List<Grouping>? {
            var groupings: MutableList<Grouping>? = null
            if (groupingAttribute != null && !groupingAttribute.trim { it <= ' ' }.isEmpty()) {
                val groupingAttribute2 = groupingAttribute.trim { it <= ' ' }
                val groupingNames = groupingAttribute2.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (groupingName in groupingNames) {
                    // get sensekey
                    val grouping = Grouping.make(groupingName)
                    if (groupings == null) {
                        groupings = ArrayList<Grouping>()
                    }
                    groupings.add(grouping)
                }
            }
            return groupings
        }
    }
}
