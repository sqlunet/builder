package org.sqlbuilder.pb;

import java.util.TreeSet;

public class PbIdSet extends TreeSet<String>
{
	@Override
	public boolean add(final String element0)
	{
		final String element = PbIdSet.toElement(element0);
		return super.add(element);
	}

	public static String toElement(final String element)
	{
		if (element == null)
			return null;
		return element.toUpperCase();
	}
}
