package org.sqlbuilder.pb.objects

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.NotNull
import org.sqlbuilder.common.Nullable
import org.sqlbuilder.common.SetCollector
import org.sqlbuilder.common.SqlId
import org.sqlbuilder.common.Utils
import org.sqlbuilder.pb.PbNormalizer
import java.lang.CharSequence
import java.util.ArrayList
import java.util.Comparator
import java.util.function.Function

class Example private constructor(
    @property:NotNull @NotNull private val roleSet: RoleSet, private val name: String, text: String,
    @property:Nullable @Nullable private val aspect: String?, @property:Nullable @Nullable private val form: String?, @property:Nullable @Nullable private val person: String?,
    @property:Nullable @Nullable private val tense: String?, @property:Nullable @Nullable private val voice: String?,
) : HasId, Insertable,
    Comparable<Example?> {

    private val text: String = PbNormalizer.normalize(text)

    @NotNull
    val rels: MutableList<Rel> = ArrayList<Rel>()

    @NotNull
    val args: MutableList<Arg> = ArrayList<Arg>()

    @RequiresIdFrom(type = Func::class)
    override fun getIntId(): Int {
        return COLLECTOR.get(this)!!
    }

    // O R D E R

    override fun compareTo(@NotNull that: Example?): Int {
        return COMPARATOR.compare(this, that)
    }

    // I N S E R T

    @RequiresIdFrom(type = RoleSet::class)
    override fun dataRow(): String {
        // (exampleid),examplename,text,aspect,form,tense,voice,person,rolesetid
        return String.format(
            "'%s','%s',%s,%s,%s,%s,%s,%d", Utils.escape(name),
            Utils.escape(text),
            SqlId.getSqlId(ASPECT_COLLECTOR[aspect]),
            SqlId.getSqlId(FORM_COLLECTOR.get(form)),
            SqlId.getSqlId(TENSE_COLLECTOR.get(tense)),
            SqlId.getSqlId(VOICE_COLLECTOR.get(voice)),
            SqlId.getSqlId(PERSON_COLLECTOR.get(person)),
            roleSet.intId
        )
    }

    override fun comment(): String {
        return String.format("%s,%s,%s,%s,%s,%s", aspect, form, tense, voice, person, roleSet.name)
    }

    override fun toString(): String {
        return String.format("%s[%s]", this.roleSet, this.name)
    }

    companion object {

        private val COMPARATOR: Comparator<Example?> = Comparator.comparing<Example?, RoleSet?>(Function { obj: Example? -> obj!!.roleSet })
            .thenComparing<String?>(Function { obj: Example? -> obj!!.name })
            .thenComparing<String?>(Function { obj: Example? -> obj!!.aspect }, Comparator.nullsFirst<String?>(Comparator.naturalOrder<String?>()))
            .thenComparing<String?>(Function { obj: Example? -> obj!!.form }, Comparator.nullsFirst<String?>(Comparator.naturalOrder<String?>()))
            .thenComparing<String?>(Function { obj: Example? -> obj!!.person }, Comparator.nullsFirst<String?>(Comparator.naturalOrder<String?>()))
            .thenComparing<String?>(Function { obj: Example? -> obj!!.tense }, Comparator.nullsFirst<String?>(Comparator.naturalOrder<String?>()))
            .thenComparing<String?>(Function { obj: Example? -> obj!!.voice }, Comparator.nullsFirst<String?>(Comparator.naturalOrder<String?>()))
            .thenComparing<String?>(Function { obj: Example? -> obj!!.text })

        private val NULLABLE_STRING_COMPARATOR: Comparator<String?> = Comparator.nullsFirst<String?>(Comparator { cs1: String?, cs2: String? -> CharSequence.compare(cs1, cs2) })

        @JvmField
        val COLLECTOR: SetCollector<Example?> = SetCollector<Example?>(COMPARATOR)

        @JvmField
        val ASPECT_COLLECTOR: SetCollector<String?> = SetCollector<String?>(NULLABLE_STRING_COMPARATOR)

        @JvmField
        val FORM_COLLECTOR: SetCollector<String?> = SetCollector<String?>(NULLABLE_STRING_COMPARATOR)

        @JvmField
        val PERSON_COLLECTOR: SetCollector<String?> = SetCollector<String?>(NULLABLE_STRING_COMPARATOR)

        @JvmField
        val TENSE_COLLECTOR: SetCollector<String?> = SetCollector<String?>(NULLABLE_STRING_COMPARATOR)

        @JvmField
        val VOICE_COLLECTOR: SetCollector<String?> = SetCollector<String?>(NULLABLE_STRING_COMPARATOR)

        // C O N S T R U C T O R
        fun make(roleSet: RoleSet, name: String, text: String, aspect: String?, form: String?, person: String?, tense: String?, voice: String?): Example {
            val e = Example(roleSet, name, text, aspect, form, person, tense, voice)

            COLLECTOR.add(e)
            if (e.aspect != null && !e.aspect.isEmpty() && e.aspect != "ns") {
                ASPECT_COLLECTOR.add(e.aspect)
            }
            if (e.form != null && !e.form.isEmpty() && e.form != "ns") {
                FORM_COLLECTOR.add(e.form)
            }
            if (e.person != null && !e.person.isEmpty() && e.person != "ns") {
                PERSON_COLLECTOR.add(e.person)
            }
            if (e.tense != null && !e.tense.isEmpty() && e.tense != "ns") {
                TENSE_COLLECTOR.add(e.tense)
            }
            if (e.voice != null && !e.voice.isEmpty() && e.voice != "ns") {
                VOICE_COLLECTOR.add(e.voice)
            }
            return e
        }

        @RequiresIdFrom(type = Func::class)
        fun getIntId(example: Example?): Int? {
            return if (example == null) null else COLLECTOR[example]
        }
    }
}
