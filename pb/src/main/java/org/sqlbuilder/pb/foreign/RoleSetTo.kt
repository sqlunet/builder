package org.sqlbuilder.pb.foreign

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.pb.objects.RoleSet
import org.sqlbuilder.pb.objects.Word
import java.util.*

abstract class RoleSetTo protected constructor(
    @JvmField val ref: String,
    pos: String,
    @JvmField val pbRoleSet: RoleSet,
    val pbWord: Word,
) : Insertable {

    enum class Db {
        VERBNET, FRAMENET
    }

    @JvmField
    val pos: String = (if ("j" == pos) "a" else pos)

    // I D E N T I T Y

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        val that = o as RoleSetTo
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

    // T O   S T R I N G

    override fun toString(): String {
        return "$ref,$pos,${pbWord.word}"
    }

    companion object {

        val COMPARATOR: Comparator<RoleSetTo> = Comparator
            .comparing<RoleSetTo, RoleSet> { it.pbRoleSet }
            .thenComparing<Word> { it.pbWord }
            .thenComparing<String> { it.ref }
            .thenComparing<String> { it.pos }

        fun make(db: Db, clazz: String, pos: String, pbRoleSet: RoleSet, word: Word): RoleSetTo {
            return if (db == Db.VERBNET) RoleSetToVn.make(clazz, pos, pbRoleSet, word)
            else (if (db == Db.FRAMENET) RoleSetToFn.make(clazz, pos, pbRoleSet, word)
            else throw IllegalArgumentException(db.name))
        }
    }
}
