package org.sqlbuilder.pb

object PbNormalizer {

    fun normalize(str0: String): String {
        var str = str0.trim { it <= ' ' }
        str = str.replace('\n', ' ')
        str = str.replace("\\s\\s+".toRegex(), " ")
        return str
    }
}
