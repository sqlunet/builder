package org.sqlbuilder.fn;

import java.io.Closeable;
import java.util.Comparator;
import java.util.TreeMap;

public class Collector<T> extends TreeMap<T, Integer> implements Closeable
{
	private boolean isOpen = false;

	public Collector(Comparator<T> comparator)
	{
		super(comparator);
	}

	public Collector<T> open()
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
			throw new IllegalThreadStateException(this + " not open");
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
