package org.semantikos.pb31.foreign

import org.semantikos.annotations.RequiresIdFrom
import org.semantikos.common.Insertable
import org.semantikos.common.Resolvable
import org.semantikos.pb31.objects.RoleSet
import org.semantikos.pb31.objects.Word
import java.util.*

abstract class RoleSetTo protected constructor(
    val ref: String,
    pos: String,
    val pbRoleSet: RoleSet,
    val pbWord: Word,
) : Insertable, Resolvable<String, Int> {

    enum class Db {
        VERBNET, FRAMENET
    }

    val pos: String = (if ("j" == pos) "a" else pos)

    // I D E N T I T Y

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that = other as RoleSetTo
        return ref == that.ref && pos == that.pos && pbRoleSet == that.pbRoleSet && pbWord == that.pbWord
    }

    override fun hashCode(): Int {
        return Objects.hash(ref, pos, pbRoleSet, pbWord)
    }

    // I N S E R T

    @RequiresIdFrom(type = RoleSet::class)
    @RequiresIdFrom(type = Word::class)
    override fun dataRow(): String {
        return "${pbRoleSet.intId},'$pos',${pbWord.intId},'$ref'"
    }

    override fun comment(): String {
        return "${pbRoleSet.name},${pbWord.word}"
    }

    // R E S O L V E

    override fun resolving(): String {
        return ref
    }

    // T O   S T R I N G

    override fun toString(): String {
        return "$ref,$pos,${pbWord.word}"
    }

    companion object {

        val COMPARATOR: Comparator<RoleSetTo> = Comparator
            .comparing<RoleSetTo, RoleSet> { it.pbRoleSet }
            .thenComparing { it.pbWord }
            .thenComparing { it.ref }
            .thenComparing { it.pos }

        fun make(db: Db, clazz: String, pos: String, pbRoleSet: RoleSet, word: Word): RoleSetTo {
            return if (db == Db.VERBNET) RoleSetToVn.make(clazz, pos, pbRoleSet, word)
            else (if (db == Db.FRAMENET) RoleSetToFn.make(clazz, pos, pbRoleSet, word)
            else throw IllegalArgumentException(db.name))
        }
    }
}
