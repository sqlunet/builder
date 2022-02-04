package org.sqlbuilder.pb.objects;

import org.sqlbuilder.common.HasId;
import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.RequiresIdFrom;
import org.sqlbuilder.common.SetCollector;
import org.sqlbuilder.pb.PbNormalizer;

import java.util.*;

public class Example implements HasId, Insertable, Comparable<Example>
{
	private static final Comparator<Example> COMPARATOR = Comparator.comparing(Example::getRoleSet) //
			.thenComparing(Example::getName) //
			.thenComparing(Example::getAspect, Comparator.nullsFirst(Comparator.naturalOrder())) //
			.thenComparing(Example::getForm, Comparator.nullsFirst(Comparator.naturalOrder())) //
			.thenComparing(Example::getPerson, Comparator.nullsFirst(Comparator.naturalOrder())) //
			.thenComparing(Example::getTense, Comparator.nullsFirst(Comparator.naturalOrder())) //
			.thenComparing(Example::getVoice, Comparator.nullsFirst(Comparator.naturalOrder())) //
			.thenComparing(Example::getText);

	public static final SetCollector<Example> COLLECTOR = new SetCollector<>(COMPARATOR);

	public static final SetCollector<String> ASPECT_COLLECTOR = new SetCollector<>(CharSequence::compare);

	public static final SetCollector<String> FORM_COLLECTOR = new SetCollector<>(CharSequence::compare);

	public static final SetCollector<String> PERSON_COLLECTOR = new SetCollector<>(CharSequence::compare);

	public static final SetCollector<String> TENSE_COLLECTOR = new SetCollector<>(CharSequence::compare);

	public static final SetCollector<String> VOICE_COLLECTOR = new SetCollector<>(CharSequence::compare);

	private final RoleSet roleSet;

	private final String name;

	private final String text;

	private final String aspect;

	private final String form;

	private final String person;

	private final String tense;

	private final String voice;

	public final List<Rel> rels;

	public final List<Arg> args;

	// C O N S T R U C T O R

	public static Example make(final RoleSet roleSet, final String name, final String text, final String aspect, final String form, final String person, final String tense, final String voice)
	{
		var e = new Example(roleSet, name, text, aspect, form, person, tense, voice);

		COLLECTOR.add(e);
		if (e.aspect != null && !e.aspect.isEmpty() && !e.aspect.equals("ns"))
		{
			Example.ASPECT_COLLECTOR.add(e.aspect);
		}
		if (e.form != null && !e.form.isEmpty() && !e.form.equals("ns"))
		{
			Example.FORM_COLLECTOR.add(e.form);
		}
		if (e.person != null && !e.person.isEmpty() && !e.person.equals("ns"))
		{
			Example.PERSON_COLLECTOR.add(e.person);
		}
		if (e.tense != null && !e.tense.isEmpty() && !e.tense.equals("ns"))
		{
			Example.TENSE_COLLECTOR.add(e.tense);
		}
		if (e.voice != null && !e.voice.isEmpty() && !e.voice.equals("ns"))
		{
			Example.VOICE_COLLECTOR.add(e.voice);
		}
		return e;
	}

	private Example(final RoleSet roleSet, final String name, final String text, final String aspect, final String form, final String person, final String tense, final String voice)
	{
		this.roleSet = roleSet;
		this.name = name;
		this.text = PbNormalizer.normalize(text);
		this.aspect = aspect;
		this.form = form;
		this.person = person;
		this.tense = tense;
		this.voice = voice;
		this.rels = new ArrayList<>();
		this.args = new ArrayList<>();
	}

	// A C C E S S

	public RoleSet getRoleSet()
	{
		return this.roleSet;
	}

	public String getName()
	{
		return this.name;
	}

	public String getText()
	{
		return this.text;
	}

	public String getAspect()
	{
		return this.aspect;
	}

	public String getForm()
	{
		return this.form;
	}

	public String getPerson()
	{
		return this.person;
	}

	public String getTense()
	{
		return this.tense;
	}

	public String getVoice()
	{
		return this.voice;
	}

	@RequiresIdFrom(type = Func.class)
	@Override
	public Integer getIntId()
	{
		return COLLECTOR.get(this);
	}

	@RequiresIdFrom(type=Func.class)
	public static Integer getIntId(final Example example)
	{
		return example == null ? null : COLLECTOR.get(example);
	}

	// O R D E R

	@Override
	public int compareTo(final Example that)
	{
		return COMPARATOR.compare(this, that);
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		// id
		// final long roleSetId = PbRoleSet.map.getId(this.roleSet);
		// final Long aspectId = this.aspect == null ? null : PbExample.aspectMap.get(this.aspect);
		// final Long formId = this.form == null ? null : PbExample.formMap.get(this.form);
		// final Long tenseId = this.tense == null ? null : PbExample.tenseMap.get(this.tense);
		// final Long voiceId = this.voice == null ? null : PbExample.voiceMap.get(this.voice);
		// final Long personId = this.person == null ? null : PbExample.personMap.get(this.person);

		// exampleid, name, text, aspect, form, tense, voice, person, rolesetid
		// Long(1, this.id);
		// String(2, this.name);
		// String(3, this.text);
		// Null(4, Types.INTEGER);
		// Long(4, aspectId);
		// Null(5, Types.INTEGER);
		// Long(5, formId);
		// Null(6, Types.INTEGER);
		// Long(6, tenseId);
		// Null(7, Types.INTEGER);
		// Long(7, voiceId);
		// Null(8, Types.INTEGER);
		// Long(8, personId);
		// Long(9, roleSetId);

		// rels
		//for (final PbRel rel : this.rels)
		//{
		//	rel.allocate();
		//	rel.insert(connection);
		//}
		//
		//// args
		//for (final PbArg arg : this.args)
		//{
		//	arg.allocate();
		//	arg.insert(connection);
		//}
		return null;
	}

	@Override
	public String toString()
	{
		return String.format("%s[%s]", this.roleSet, this.name);
	}
}
