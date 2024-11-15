package org.sqlbuilder.pb.foreign

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.Resolvable
import org.sqlbuilder.pb.objects.RoleSet
import org.sqlbuilder.pb.objects.Word
import java.util.*
import java.util.function.Function

abstract class Alias protected constructor(
	@JvmField val ref: String,
    pos: String,
	@JvmField val pbRoleSet: RoleSet,
    val pbWord: Word
) : Insertable, Resolvable<String, Int> {

    enum class Db {
        VERBNET, FRAMENET
    }

    @JvmField
    val pos: String = (if ("j" == pos) "a" else pos)!!

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as Alias
        return ref == that.ref && pos == that.pos && pbRoleSet == that.pbRoleSet && pbWord == that.pbWord
    }

    override fun hashCode(): Int {
        return Objects.hash(ref, pos, pbRoleSet, this.pbWord)
    }

    // I N S E R T

    @RequiresIdFrom(type = RoleSet::class)
    @RequiresIdFrom(type = Word::class)
    override fun dataRow(): String {
        // rolesetid,refid,ref,pos,pbwordid
        return String.format(
            "%d,'%s',%d,'%s'",
            pbRoleSet.intId,
            pos,
            pbWord.intId,
            ref
        )
    }

    override fun comment(): String {
        return String.format("%s,%s", pbRoleSet.name, pbWord.word)
    }

    // R E S O L V E

    override fun resolving(): String {
        return ref
    }

    override fun toString(): String {
        return String.format("%s,%s,%s", ref, pos, pbWord.word)
    }

    companion object {

        val COMPARATOR: Comparator<Alias?> = Comparator
            .comparing<Alias?, RoleSet?>{ it!!.pbRoleSet }
            .thenComparing<Word?> { it!!.pbWord }
            .thenComparing<String?> { it.ref }
            .thenComparing<String?> { it.pos }

        fun make(db: Db, clazz: String, pos: String, pbRoleSet: RoleSet, word: Word): Alias {
            return if (db == Db.VERBNET) VnAlias.make(clazz, pos, pbRoleSet, word) else (if (db == Db.FRAMENET) FnAlias.make(clazz, pos, pbRoleSet, word) else throw IllegalArgumentException(db.name))
        }
    }
}
