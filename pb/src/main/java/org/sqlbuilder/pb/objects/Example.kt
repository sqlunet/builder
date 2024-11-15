package org.sqlbuilder.pb.objects;

import org.sqlbuilder.annotations.RequiresIdFrom;
import org.sqlbuilder.common.*;
import org.sqlbuilder.pb.PbNormalizer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

	private static final Comparator<String> NULLABLE_STRING_COMPARATOR = Comparator.nullsFirst(CharSequence::compare);

	public static final SetCollector<Example> COLLECTOR = new SetCollector<>(COMPARATOR);

	public static final SetCollector<String> ASPECT_COLLECTOR = new SetCollector<>(NULLABLE_STRING_COMPARATOR);

	public static final SetCollector<String> FORM_COLLECTOR = new SetCollector<>(NULLABLE_STRING_COMPARATOR);

	public static final SetCollector<String> PERSON_COLLECTOR = new SetCollector<>(NULLABLE_STRING_COMPARATOR);

	public static final SetCollector<String> TENSE_COLLECTOR = new SetCollector<>(NULLABLE_STRING_COMPARATOR);

	public static final SetCollector<String> VOICE_COLLECTOR = new SetCollector<>(NULLABLE_STRING_COMPARATOR);

	@NotNull
	private final RoleSet roleSet;

	private final String name;

	private final String text;

	@Nullable
	private final String aspect;

	@Nullable
	private final String form;

	@Nullable
	private final String person;

	@Nullable
	private final String tense;

	@Nullable
	private final String voice;

	@NotNull
	public final List<Rel> rels;

	@NotNull
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

	private Example(@NotNull final RoleSet roleSet, final String name, final String text, @Nullable final String aspect, @Nullable final String form, @Nullable final String person, @Nullable final String tense, @Nullable final String voice)
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

	@NotNull
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

	@Nullable
	public String getAspect()
	{
		return this.aspect;
	}

	@Nullable
	public String getForm()
	{
		return this.form;
	}

	@Nullable
	public String getPerson()
	{
		return this.person;
	}

	@Nullable
	public String getTense()
	{
		return this.tense;
	}

	@Nullable
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

	@RequiresIdFrom(type = Func.class)
	public static Integer getIntId(final Example example)
	{
		return example == null ? null : COLLECTOR.get(example);
	}

	// O R D E R

	@Override
	public int compareTo(@NotNull final Example that)
	{
		return COMPARATOR.compare(this, that);
	}

	// I N S E R T

	@RequiresIdFrom(type = RoleSet.class)
	@Override
	public String dataRow()
	{
		// (exampleid),examplename,text,aspect,form,tense,voice,person,rolesetid
		return String.format("'%s','%s',%s,%s,%s,%s,%s,%d", Utils.escape(name), //
				Utils.escape(text), //
				SqlId.getSqlId(Example.ASPECT_COLLECTOR.get(aspect)), //
				SqlId.getSqlId(Example.FORM_COLLECTOR.get(form)), //
				SqlId.getSqlId(Example.TENSE_COLLECTOR.get(tense)), //
				SqlId.getSqlId(Example.VOICE_COLLECTOR.get(voice)), //
				SqlId.getSqlId(Example.PERSON_COLLECTOR.get(person)), //
				roleSet.getIntId() //
		);
	}

	@Override
	public String comment()
	{
		return String.format("%s,%s,%s,%s,%s,%s", aspect, form, tense, voice, person, roleSet.getName());
	}

	@Override
	public String toString()
	{
		return String.format("%s[%s]", this.roleSet, this.name);
	}
}
