package org.sqlbuilder.pb.objects;

import org.sqlbuilder.common.*;

import java.util.Comparator;
import java.util.Properties;

public class Func implements HasId, Comparable<Func>, Insertable
{
	public static final Comparator<Func> COMPARATOR = Comparator.comparing(Func::getFunc);

	public static final SetCollector<Func> COLLECTOR = new SetCollector<>(COMPARATOR);

	private static final String[] PREDEFINED = new String[]{"ADV", "AV", "CAU", "DIR", "DIS", "DS", "DSP", "EXT", "LOC", "MNR", "MOD", "NEG", "PNC", "PRD", "PRED", "PRP", "Q", "RCL", "REC", "SLC", "STR", "TMP"};

	private static final Properties DESCRIPTIONS = new Properties();

	static
	{
		DESCRIPTIONS.setProperty("ADV", "adverbial modification");
		DESCRIPTIONS.setProperty("CAU", "cause");
		DESCRIPTIONS.setProperty("DIR", "direction");
		DESCRIPTIONS.setProperty("EXT", "extent");
		DESCRIPTIONS.setProperty("LOC", "location");
		DESCRIPTIONS.setProperty("MNR", "manner");
		DESCRIPTIONS.setProperty("MOD", "general modification");
		DESCRIPTIONS.setProperty("NEG", "negation");
		DESCRIPTIONS.setProperty("PNC", "purpose no cause");
		DESCRIPTIONS.setProperty("PRD", "secondary predication");
		DESCRIPTIONS.setProperty("PRP", "purpose (deprecated)");
		DESCRIPTIONS.setProperty("Q", "quantity");
		DESCRIPTIONS.setProperty("RCL", "relative clause");
		DESCRIPTIONS.setProperty("REC", "reciprocal");
		DESCRIPTIONS.setProperty("TMP", "temporal");
	}

	private final String func;

	// C O N S T R U C T O R

	public static Func make(final String f)
	{
		if (f == null || f.isEmpty())
		{
			return null;
		}
		var fn = new Func(f);
		COLLECTOR.add(fn);
		return fn;
	}

	private Func(final String funcName)
	{
		this.func = normalize(funcName);
	}

	private static String normalize(final String funcName)
	{
		for (final String predefined : Func.PREDEFINED)
		{
			if (predefined.equalsIgnoreCase(funcName))
			{
				return predefined;
			}
		}
		return funcName.toLowerCase();
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

	@RequiresIdFrom(type = Func.class)
	public static Integer getIntId(final Func func)
	{
		return func == null ? null : COLLECTOR.get(func);
	}

	// O R D E R

	@Override
	public int compareTo(final Func that)
	{
		return COMPARATOR.compare(this, that);
	}

	// I N S E R T

	@Override
	public String dataRow()
	{
		return String.format("'%s',%s", func, Utils.nullableQuotedString(DESCRIPTIONS.getProperty(func, null)));
	}

	@Override
	public String toString()
	{
		return "f:" + func;
	}
}
