package org.sqlbuilder2.legacy

import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.ParseException
import java.io.Serializable
import kotlin.Throws

data class SynsetToSynsetMapping(
    val from: Long,
    val to: Long
) : Insertable, Serializable {

    override fun dataRow(): String {
        return "$from,$to"
    }

    companion object {

        //1740 00001740 1 m
        @JvmStatic
        @Throws(ParseException::class)
        fun parse(line: String): SynsetToSynsetMapping {
            try {
                val fields: Array<String> = line.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val synset1Id = fields[0].toLong()
                val synset2Id = fields[1].toLong()
                return SynsetToSynsetMapping(synset1Id, synset2Id)
            } catch (e: Exception) {
                throw ParseException(e.message!!)
            }
        }
    }
}
