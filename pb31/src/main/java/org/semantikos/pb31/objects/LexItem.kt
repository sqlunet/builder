package org.semantikos.pb31.objects

import java.util.*

open class LexItem(lemma: String) : Comparable<LexItem> {

    val word: String = lemma

    fun put() {
        val keyExisted = map.containsKey(this)
        map.put(this, Word.make(word))
        if (keyExisted) {
            throw RuntimeException(toString())
        }
    }

    // O R D E R

    override fun compareTo(p: LexItem): Int {
        return word.compareTo(p.word)
    }

    // T O S T R I N G

    override fun toString(): String {
        return word
    }

    companion object {

        val map: MutableMap<LexItem, Word> = TreeMap<LexItem, Word>()

        fun make(lemma: String): LexItem {
            return LexItem(lemma)
        }
    }
}
