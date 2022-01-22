package org.sqlbuilder.fn.types;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Cxns
{
	// cxns.table=fncxns
	// cxns.create=CREATE TABLE IF NOT EXISTS %Fn_cxns.table% ( cxnid INTEGER NOT NULL,cxn VARCHAR(32),PRIMARY KEY (cxnid) );

	public static final Set<String> SET = new HashSet<>();

	public static Map<String, Integer> MAP;

	public static void record(String type)
	{
		SET.add(type);
	}

	public static Object getId(String value)
	{
		Integer id = MAP.get(value);
		if (id != null)
		{
			return id;
		}
		return "NULL";
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
