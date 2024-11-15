package org.sqlbuilder.pb.objects

import java.util.*

open class LexItem(lemma: String) : Comparable<LexItem> {

    val word: String = lemma

    fun put() {
        val keyExisted = map.containsKey(this)
        map.put(this, Word.make(this.word))
        if (keyExisted) {
            throw RuntimeException(toString())
        }
    }

    // O R D E R

    override fun compareTo(p: LexItem): Int {
        return this.word.compareTo(p.word)
    }

    // T O S T R I N G

    override fun toString(): String {
        return String.format("%s", this.word)
    }

    companion object {

        @JvmField
        val map: MutableMap<LexItem?, Word?> = TreeMap<LexItem?, Word?>()

        fun make(lemma: String): LexItem {
            return LexItem(lemma)
        }
    }
}
