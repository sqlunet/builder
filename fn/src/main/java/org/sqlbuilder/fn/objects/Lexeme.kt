package org.sqlbuilder.fn.objects

import edu.berkeley.icsi.framenet.LexemeType
import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.Utils.zeroableInt
import org.sqlbuilder.fn.objects.Word.Companion.make
import java.util.*

class Lexeme private constructor(lexeme: LexemeType, val luid: Long) : Insertable {

    private val pos: Int = lexeme.getPOS().intValue()

    private val breakBefore: Boolean = lexeme.getBreakBefore()

    private val headWord: Boolean = lexeme.getHeadword()

    private val order: Int = lexeme.getOrder()

    private val word: Word = make(trim(lexeme.getName()))

    // A C C E S S

    fun getWord(): String {
        return word.word
    }

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as Lexeme
        return word == that.word && luid == that.luid
    }

    override fun hashCode(): Int {
        return Objects.hash(word, luid)
    }

    // I N S E R T

    @RequiresIdFrom(type = Word::class)
    override fun dataRow(): String {
        return "${word.getSqlId()},$pos,${if (breakBefore) 1 else 0},${if (headWord) 1 else 0},${zeroableInt(order)},$luid"
    }

    override fun comment(): String {
        return "word=${getWord()}"
    }

    // T O S T R I N G

    override fun toString(): String {
        return "[LEX word=${getWord()} luid=$luid]"
    }

    companion object {

        @JvmField
        val COMPARATOR: Comparator<Lexeme> = Comparator
            .comparing<Lexeme, String> { it.getWord() }
            .thenComparing<Long> { it.luid }

        @JvmField
        val SET = HashSet<Lexeme>()

        fun make(lexeme: LexemeType, luid: Long): Lexeme {
            val l = Lexeme(lexeme, luid)
            SET.add(l)
            return l
        }

        private fun trim(string: String): String {
            return string.replace("_*\\(.*$".toRegex(), "")
        }

        // W O R D
        /* frame
        name="construction(entity)"
        name="power_((statistical))"
        name="talk_(to)"
        name="Indian((American))"
        name="practice_((mass))"
        name="rehearsal_((mass))"
        name="late_((at_night))"
        */
        /* lexunit
        name="practice_((mass))"
        name="rehearsal_((mass))"
        name="Indian((American))"
        name="construction(entity)"
        name="talk_(to)"
        name="power_((statistical))"
        name="late_((at_night))"
        */
    }
}
