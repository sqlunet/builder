package org.sqlbuilder.fn.types;

import org.sqlbuilder.fn.Collector;
import org.sqlbuilder.fn.RequiresIdFrom;

import java.util.Comparator;

public class Cxns
{
	// cxns.table=fncxns
	// cxns.create=CREATE TABLE IF NOT EXISTS %Fn_cxns.table% ( cxnid INTEGER NOT NULL,cxn VARCHAR(32),PRIMARY KEY (cxnid) );

	public static final Comparator<String> COMPARATOR = Comparator.naturalOrder();

	public static final Collector<String> COLLECTOR = new Collector<>(COMPARATOR);

	public static void add(String type)
	{
		COLLECTOR.add(type);
	}

	@RequiresIdFrom(type = Cxns.class)
	public static Integer getIntId(String value)
	{
		return value == null ? null : COLLECTOR.get(value);
	}

	@RequiresIdFrom(type = Cxns.class)
	public static Object getSqlId(String value)
	{
		return Util.getSqlId(getIntId(value));
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
}
