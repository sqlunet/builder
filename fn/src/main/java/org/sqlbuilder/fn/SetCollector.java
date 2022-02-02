package org.sqlbuilder.fn;

import java.io.Closeable;
import java.util.Comparator;
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

	public boolean add(T item)
	{
		return put(item, null) == null;
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
		return ":" + size();
	}
}
