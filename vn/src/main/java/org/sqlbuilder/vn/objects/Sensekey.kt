package org.sqlbuilder.vn.objects

class Sensekey private constructor(
    @JvmField val sensekey: String,
    private val isDefinite: Boolean,
) : Comparable<Sensekey> {

    val quality: Float
        get() = if (this.isDefinite) 1f else .5f

    override fun compareTo(that: Sensekey): Int {
        return sensekey.compareTo(that.sensekey)
    }

    companion object {

        @JvmStatic
        fun parse(str0: String): Sensekey {
            // handle question mark
            var str = str0
            var isDefiniteFlag = true
            if (str.startsWith("?")) {
                isDefiniteFlag = false
                str = str.substring(1)
            }

            val senseKey = "$str::"
            return Sensekey(senseKey, isDefiniteFlag)
        }
    }
}
