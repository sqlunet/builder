package org.sqlbuilder.pb.objects;

import org.sqlbuilder.common.HasId;
import org.sqlbuilder.common.RequiresIdFrom;
import org.sqlbuilder.common.SetCollector;

import java.util.*;

public class Func implements HasId, Comparable<Func>
{
	public static final Comparator<Func> COMPARATOR = Comparator.comparing(Func::getFunc);

	public static final SetCollector<Func> COLLECTOR = new SetCollector<>(COMPARATOR);

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

	public static final Properties funcNames = new Properties();

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

	private final String func;

	// C O N S T R U C T O R

	public static Func make(final String funcName)
	{
		var f = new Func(funcName);
		COLLECTOR.add(f);
		return f;
	}

	private Func(final String funcName)
	{
		this.func = normalize(funcName);
	}

	// A C C E S S

	public String getFunc()
	{
		return func;
	}

	@RequiresIdFrom(type = Func.class)
	@Override
	public Integer getIntId()
	{
		return COLLECTOR.get(this);
	}

	// O R D E R

	@Override
	public int compareTo(final Func that)
	{
		return COMPARATOR.compare(this, that);
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
}
