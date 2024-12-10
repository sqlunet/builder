package org.sqlbuilder.pb31.objects

class Predicate private constructor(val head: String, lemma: String) : LexItem(lemma) {

    companion object {

        fun make(head: String, lemma: String): Predicate {
            return Predicate(head, lemma)
        }
    }
}
