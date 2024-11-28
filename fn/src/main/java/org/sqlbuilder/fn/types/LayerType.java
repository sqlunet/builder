package org.sqlbuilder.fn.types;

import org.sqlbuilder.annotations.RequiresIdFrom;
import org.sqlbuilder.common.SetCollector;
import org.sqlbuilder.common.SqlId;

import java.util.Comparator;

public class LayerType
{
	public static final Comparator<String> COMPARATOR = Comparator.naturalOrder();

	public static final SetCollector<String> COLLECTOR = new SetCollector<>(COMPARATOR);

	public static void add(String type)
	{
		COLLECTOR.add(type);
	}

	@RequiresIdFrom(type = LayerType.class)
	public static Integer getIntId(String value)
	{
		return value == null ? null : COLLECTOR.apply(value);
	}

	@RequiresIdFrom(type = LayerType.class)
	public static Object getSqlId(String value)
	{
		return SqlId.getSqlId(getIntId(value));
	}
}

/*
# layertypeid, layertype
1, Adj
2, Adv
3, Art
4, BNC
5, CE
6, CEE
7, CstrPT
8, FE
9, GF
10, GovX
11, NER
12, Noun
13, Other
14, PENN
15, Prep
16, PT
17, Scon
18, Sent
19, Target
20, Verb
21, WSL
 */

