package org.sqlbuilder.pb;

import org.sqlbuilder.common.Insertable;

import java.util.*;

public class PbExample implements Insertable<PbExample>, Comparable<PbExample>
{
	protected static final Set<PbExample> SET = new TreeSet<>();

	protected static final SortedSet<String> aspectSet = new TreeSet<>();
	protected static Map<String, Integer> aspectMap;

	protected static final SortedSet<String> formSet = new TreeSet<>();
	protected static Map<String, Integer> formMap;

	protected static final SortedSet<String> personSet = new TreeSet<>();
	protected static Map<String, Integer> personMap;

	protected static final SortedSet<String> tenseSet = new TreeSet<>();
	protected static Map<String, Integer> tenseMap;

	protected static final SortedSet<String> voiceSet = new TreeSet<>();
	protected static Map<String, Integer> voiceMap;

	private final PbRoleSet roleSet;

	private final String name;

	private final String text;

	private final String aspect;

	private final String form;

	private final String person;

	private final String tense;

	private final String voice;

	public final List<PbRel> rels;

	public final List<PbArg> args;

	public PbExample(final PbRoleSet roleSet, final String name, final String text, final String aspect, final String form, final String person, final String tense, final String voice)
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

		SET.add(this);
		if (this.aspect != null && !this.aspect.isEmpty() && !this.aspect.equals("ns"))
		{
			PbExample.aspectSet.add(this.aspect);
		}
		if (this.form != null && !this.form.isEmpty() && !this.form.equals("ns"))
		{
			PbExample.formSet.add(this.form);
		}
		if (this.person != null && !this.person.isEmpty() && !this.person.equals("ns"))
		{
			PbExample.personSet.add(this.person);
		}
		if (this.tense != null && !this.tense.isEmpty() && !this.tense.equals("ns"))
		{
			PbExample.tenseSet.add(this.tense);
		}
		if (this.voice != null && !this.voice.isEmpty() && !this.voice.equals("ns"))
		{
			PbExample.voiceSet.add(this.voice);
		}
	}

	public static PbExample make(final PbRoleSet roleSet, final String name, final String text, final String aspect, final String form, final String person, final String tense, final String voice)
	{
		return new PbExample(roleSet, name, text, aspect, form, person, tense, voice);
	}

	public PbRoleSet getRoleSet()
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

	// O R D E R

	private static final Comparator<PbExample> COMPARATOR = Comparator.comparing(PbExample::getRoleSet) //
			.thenComparing(PbExample::getName) //
			.thenComparing(PbExample::getAspect, Comparator.nullsFirst(Comparator.naturalOrder())) //
			.thenComparing(PbExample::getForm, Comparator.nullsFirst(Comparator.naturalOrder())) //
			.thenComparing(PbExample::getPerson, Comparator.nullsFirst(Comparator.naturalOrder())) //
			.thenComparing(PbExample::getTense, Comparator.nullsFirst(Comparator.naturalOrder())) //
			.thenComparing(PbExample::getVoice, Comparator.nullsFirst(Comparator.naturalOrder())) //
			.thenComparing(PbExample::getText);

	@Override
	public int compareTo(final PbExample that)
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
