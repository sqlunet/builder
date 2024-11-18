package org.sqlbuilder.pb.objects

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.*
import org.sqlbuilder.pb.PbNormalizer

class Example private constructor(
    private val roleSet: RoleSet,
    private val name: String,
    text: String,
    private val aspect: String?,
    private val form: String?,
    private val person: String?,
    private val tense: String?,
    private val voice: String?,
) : HasId, Insertable, Comparable<Example> {

    private val text: String = PbNormalizer.normalize(text)

    val rels: MutableList<Rel> = ArrayList<Rel>()

    val args: MutableList<Arg> = ArrayList<Arg>()

    // N I D

    @RequiresIdFrom(type = Func::class)
    override fun getIntId(): Int {
        return COLLECTOR[this]!!
    }

    // O R D E R

    override fun compareTo(that: Example): Int {
        return COMPARATOR.compare(this, that)
    }

    // I N S E R T

    @RequiresIdFrom(type = RoleSet::class)
    override fun dataRow(): String {
        return "'${Utils.escape(text)}','${SqlId.getSqlId(ASPECT_COLLECTOR[aspect])}',${SqlId.getSqlId(FORM_COLLECTOR[form])},${SqlId.getSqlId(TENSE_COLLECTOR[tense])},${SqlId.getSqlId(VOICE_COLLECTOR[voice])},${SqlId.getSqlId(PERSON_COLLECTOR[person])},${roleSet.intId}"
    }

    override fun comment(): String {
        return "$aspect,$form,$tense,$voice,$person,${roleSet.name}"
    }

    override fun toString(): String {
        return "$roleSet[$name]"
    }

    companion object {

        private val COMPARATOR: Comparator<Example> = Comparator
            .comparing<Example, RoleSet> { it.roleSet }
            .thenComparing { it.name }
            .thenComparing({ it.aspect }, Comparator.nullsFirst<String?>(Comparator.naturalOrder()))
            .thenComparing({ it.form }, Comparator.nullsFirst<String?>(Comparator.naturalOrder()))
            .thenComparing({ it.person }, Comparator.nullsFirst<String?>(Comparator.naturalOrder()))
            .thenComparing({ it.tense }, Comparator.nullsFirst<String?>(Comparator.naturalOrder()))
            .thenComparing({ it.voice }, Comparator.nullsFirst<String?>(Comparator.naturalOrder()))
            .thenComparing { it.text }

        private val NULLABLE_STRING_COMPARATOR: Comparator<String?> = Comparator.nullsFirst<String?>(Comparator { s1: String, s2: String -> s1.compareTo(s2) })

        @JvmField
        val COLLECTOR = SetCollector<Example>(COMPARATOR)

        @JvmField
        val ASPECT_COLLECTOR = SetCollector<String>(NULLABLE_STRING_COMPARATOR)

        @JvmField
        val FORM_COLLECTOR = SetCollector<String>(NULLABLE_STRING_COMPARATOR)

        @JvmField
        val PERSON_COLLECTOR = SetCollector<String>(NULLABLE_STRING_COMPARATOR)

        @JvmField
        val TENSE_COLLECTOR = SetCollector<String>(NULLABLE_STRING_COMPARATOR)

        @JvmField
        val VOICE_COLLECTOR = SetCollector<String>(NULLABLE_STRING_COMPARATOR)

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

        @Suppress("unused")
        @RequiresIdFrom(type = Func::class)
        fun getIntId(example: Example): Int {
            return COLLECTOR[example]!!
        }
    }
}
