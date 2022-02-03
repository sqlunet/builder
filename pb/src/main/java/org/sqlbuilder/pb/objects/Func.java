package org.sqlbuilder.pb.objects;

import java.util.*;

public class Func implements Comparable<Func>
{
	public static final Set<Func> SET = new HashSet<>();

	public static Map<Func, Integer> MAP;

	// A secondary agent
	// ADV adverbial modification
	// CAU cause
	// DIR direction
	// DIS
	// DSP
	// EXT extent
	// LOC location
	// MNR manner
	// MOD general modification
	// NEG negation
	// PNC purpose no cause
	// PRD secondary predication
	// PRP purpose (deprecated)
	// Q quantity
	// RCL relative clause
	// REC reciprocal
	// SLC
	// STR
	// TMP temporal
	private static final String[] cats = new String[]{"ADV", "AV", "CAU", "DIR", "DIS", "DS", "DSP", "EXT", "LOC", "MNR", "MOD", "NEG", "PNC", "PRD", "PRED", "PRP", "Q", "RCL", "REC", "SLC", "STR", "TMP"};

	protected static final Properties funcNames = new Properties();

	static
	{
		Func.funcNames.setProperty("ADV", "adverbial modification");
		Func.funcNames.setProperty("CAU", "cause");
		Func.funcNames.setProperty("DIR", "direction");
		Func.funcNames.setProperty("EXT", "extent");
		Func.funcNames.setProperty("LOC", "location");
		Func.funcNames.setProperty("MNR", "manner");
		Func.funcNames.setProperty("MOD", "general modification");
		Func.funcNames.setProperty("NEG", "negation");
		Func.funcNames.setProperty("PNC", "purpose no cause");
		Func.funcNames.setProperty("PRD", "secondary predication");
		Func.funcNames.setProperty("PRP", "purpose (deprecated)");
		Func.funcNames.setProperty("Q", "quantity");
		Func.funcNames.setProperty("RCL", "relative clause");
		Func.funcNames.setProperty("REC", "reciprocal");
		Func.funcNames.setProperty("TMP", "temporal");
	}

	private static String normalize(final String funcName)
	{
		for (final String cat : Func.cats)
		{
			if (cat.equalsIgnoreCase(funcName))
			{
				return cat;
			}
		}
		return funcName.toLowerCase();
	}

	private final String pbfunc;

	// C O N S T R U C T O R

	public Func(final String funcName)
	{
		this.pbfunc = normalize(funcName);
		SET.add(this);
	}

	// O R D E R

	@Override
	public int compareTo(final Func that)
	{
		return pbfunc.compareTo(that.pbfunc);
	}
}
