package org.semantikos.pb.objects

class Predicate private constructor(val head: String, lemma: String) : LexItem(lemma) {

    companion object {

        fun make(head: String, lemma: String): Predicate {
            return Predicate(head, lemma)
        }
    }
}
