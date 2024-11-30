package org.sqlbuilder.fn.types;

import org.sqlbuilder.common.HasID;
import org.sqlbuilder.common.Insertable;
import org.sqlbuilder.common.Utils;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Cxns implements HasID, Insertable
{
	public static final Comparator<Cxns> COMPARATOR = Comparator.comparing(Cxns::getName).thenComparing(Cxns::getId);

	public static final Set<Cxns> SET = new HashSet<>();

	private final int id;

	private final String name;

	// C O N S T R U C T O R

	@SuppressWarnings("UnusedReturnValue")
	public static Cxns make(final int id, final String name)
	{
		var c = new Cxns(id, name);
		SET.add(c);
		return c;
	}

	private Cxns(final int id, final String name)
	{
		this.id = id;
		this.name = name;
	}

	// A C C E S S

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	// I D E N T I T Y

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		Cxns cxns = (Cxns) o;
		return id == cxns.id && name.equals(cxns.name);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id, name);
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("%d,'%s'", id, Utils.escape(name));
	}
}

/*
# cxnid, cxn
78, Ones_very_eyes
80, As.role
74, Subject-predicate
75, Head-complements
79, Bare_noun_phrase.role
76, Be_present-participle
73, Valence_sharing.raising
83, Superlative
117, Uniqueness
26, Supplement_specificational
105, Coordination
27, Supplement_ascriptional
112, Stripping
114, There_be_a_time_when
113, Bare_argument_ellipsis
102, Postpositive_adjective
31, Attributive_degree_modification
18, Comparison_equality
107, Noun-noun_compound
82, Determined_noun_phrase
111, TEMP_The_ubiquitous_noun
81, Modifier-head
*/
