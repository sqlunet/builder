package org.sqlbuilder.fn.types;

import org.sqlbuilder.fn.SetCollector;
import org.sqlbuilder.fn.RequiresIdFrom;

import java.util.Comparator;

/*
fetypes.table=fnfetypes
fetypes.create=CREATE TABLE IF NOT EXISTS %Fn_fetypes.table% ( fetypeid INTEGER NOT NULL AUTO_INCREMENT,fetype VARCHAR(30),PRIMARY KEY (fetypeid) );
fetypes.unq1=CREATE UNIQUE INDEX IF NOT EXISTS unq_%Fn_fetypes.table%_fetype ON %Fn_fetypes.table% (fetype);
fetypes.no-unq1=DROP INDEX IF EXISTS unq_%Fn_fetypes.table%_fetype;
 */
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
		return Util.getSqlId(getIntId(value));
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
}
