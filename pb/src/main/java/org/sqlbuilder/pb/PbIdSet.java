package org.sqlbuilder.pb;

import java.util.TreeSet;

public class PbIdSet extends TreeSet<String>
{
	private static final long serialVersionUID = 1L;

	@Override
	public boolean add(final String element0)
	{
		final String element = PbIdSet.toElement(element0);
		return super.add(element);
	}

	static String toElement(final String element)
	{
		if (element == null)
			return null;
		return element.toUpperCase();
	}
}
