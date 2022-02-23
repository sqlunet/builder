package org.sqlbuilder.common;

import java.io.Closeable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;

public class SetCollector<T> extends TreeMap<T, Integer> implements Closeable
{
	private boolean isOpen = false;

	public SetCollector(Comparator<T> comparator)
	{
		super(comparator);
	}

	public SetCollector<T> open()
	{
		int i = 1;
		for (var k : keySet())
		{
			put(k, i++);
		}
		isOpen = true;
		//System.err.println("[OPEN]:" + size());
		return this;
	}

	/**
	 * Add item key to map
	 *
	 * @param item item key
	 * @return false if already there
	 */
	public boolean add(T item)
	{
		// avoid changing value to null
		// putIfAbsent(item, null) uses get and throw not-open exception
		if (containsKey(item))
		{
			return false;
		}
		return put(item, null) == null; // null if there was no mapping
	}

	@Override
	public Integer get(final Object key)
	{
		if (!isOpen)
		{
			throw new IllegalStateException(this + " not open");
		}
		return super.get(key);
	}

	@Override
	public void close()
	{
		isOpen = false;
		clear();
		// System.err.println("[CLOSE]:" + size());
	}

	public String status()
	{
		return "#" + size();
	}

	public HashMap<T, Integer> toHashMap()
	{
		return new HashMap<>(this);
	}
}
