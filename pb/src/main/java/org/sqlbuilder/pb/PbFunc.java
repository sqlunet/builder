package org.sqlbuilder.pb;

import java.util.Map;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;

public class PbFunc implements Comparable<PbFunc>
{
	protected static final SortedSet<PbFunc> SET = new TreeSet<>();

	public static Map<PbFunc, Integer> MAP;

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
		PbFunc.funcNames.setProperty("ADV", "adverbial modification");
		PbFunc.funcNames.setProperty("CAU", "cause");
		PbFunc.funcNames.setProperty("DIR", "direction");
		PbFunc.funcNames.setProperty("EXT", "extent");
		PbFunc.funcNames.setProperty("LOC", "location");
		PbFunc.funcNames.setProperty("MNR", "manner");
		PbFunc.funcNames.setProperty("MOD", "general modification");
		PbFunc.funcNames.setProperty("NEG", "negation");
		PbFunc.funcNames.setProperty("PNC", "purpose no cause");
		PbFunc.funcNames.setProperty("PRD", "secondary predication");
		PbFunc.funcNames.setProperty("PRP", "purpose (deprecated)");
		PbFunc.funcNames.setProperty("Q", "quantity");
		PbFunc.funcNames.setProperty("RCL", "relative clause");
		PbFunc.funcNames.setProperty("REC", "reciprocal");
		PbFunc.funcNames.setProperty("TMP", "temporal");
	}

	private static String normalize(final String funcName)
	{
		for (final String cat : PbFunc.cats)
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

	public PbFunc(final String funcName)
	{
		this.pbfunc = normalize(funcName);
		SET.add(this);
	}

	// O R D E R

	@Override
	public int compareTo(final PbFunc that)
	{
		return pbfunc.compareTo(that.pbfunc);
	}
}
