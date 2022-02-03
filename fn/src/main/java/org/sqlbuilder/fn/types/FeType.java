package org.sqlbuilder.fn.types;

import org.sqlbuilder.common.SetCollector;
import org.sqlbuilder.common.RequiresIdFrom;
import org.sqlbuilder.common.SqlId;

import java.util.Comparator;

public class FeType
{
	public static final Comparator<String> COMPARATOR = String::compareToIgnoreCase;

	public static final SetCollector<String> COLLECTOR = new SetCollector<>(COMPARATOR);

	@RequiresIdFrom(type=FeType.class)
	public static Integer getIntId(String value)
	{
		return value == null ? null : COLLECTOR.get(value);
	}

	@RequiresIdFrom(type=FeType.class)
	public static Object getSqlId(String value)
	{
		return SqlId.getSqlId(getIntId(value));
	}
}

/*
# fetypeid, fetype
1, Abundant_entities
2, Abuser
3, Accessibility
4, Accessory
5, Accoutrement
6, Accuracy
7, Accused
8, Act
9, Action
 */

