package org.sqlbuilder.pb.objects

import org.sqlbuilder.annotations.RequiresIdFrom
import org.sqlbuilder.common.HasId
import org.sqlbuilder.common.Insertable
import org.sqlbuilder.common.SetCollector
import org.sqlbuilder.common.SqlId
import org.sqlbuilder.common.Utils.escape
import org.sqlbuilder.common.Utils.nullable
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
        return COLLECTOR.invoke(this)
    }

    // O R D E R

    override fun compareTo(that: Example): Int {
        return COMPARATOR.compare(this, that)
    }

    // I N S E R T

    @RequiresIdFrom(type = RoleSet::class)
    override fun dataRow(): String {
        val aspectId = SqlId.getSqlId(if (aspect != null && aspect != "ns") ASPECT_COLLECTOR.invoke(aspect) else null)
        val formId = SqlId.getSqlId(if (form != null && form != "ns") FORM_COLLECTOR.invoke(form) else null)
        val tenseId = SqlId.getSqlId(if (tense != null && tense != "ns") TENSE_COLLECTOR.invoke(tense) else null)
        val voiceId = SqlId.getSqlId(if (voice != null && voice != "ns") VOICE_COLLECTOR.invoke(voice) else null)
        val personId = SqlId.getSqlId(if (person != null && person != "ns") PERSON_COLLECTOR.invoke(person) else null)
        return "'${escape(name)}','${escape(text)}',${nullable(aspectId)},${nullable(formId)},${nullable(tenseId)},${nullable(voiceId)},${nullable(personId)},${roleSet.intId}"
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

        private val STRING_COMPARATOR = Comparator { s1: String, s2: String -> s1.compareTo(s2) }

        val COLLECTOR = SetCollector<Example>(COMPARATOR)

        val ASPECT_COLLECTOR = SetCollector<String>(STRING_COMPARATOR)

        val FORM_COLLECTOR = SetCollector<String>(STRING_COMPARATOR)

        val PERSON_COLLECTOR = SetCollector<String>(STRING_COMPARATOR)

        val TENSE_COLLECTOR = SetCollector<String>(STRING_COMPARATOR)

        val VOICE_COLLECTOR = SetCollector<String>(STRING_COMPARATOR)

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
            return COLLECTOR.invoke(example)
        }
    }
}
