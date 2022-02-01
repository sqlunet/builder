package org.sqlbuilder.fn;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.IdentityHashMap;

public class ListCollector<T> extends ArrayList<T> implements Closeable
{
	private boolean isOpen = false;

	private int allocator = 0;

	private IdentityHashMap<T, Integer> map = new IdentityHashMap<>();

	public ListCollector()
	{
		super();
	}

	public ListCollector<T> open()
	{
		return this;
	}

	@Override
	public boolean add(T item)
	{
		map.put(item, ++allocator);
		return super.add(item);
	}

	public Integer get(final Object key)
	{
		var idx = map.get(key);
		//assert indexOf(key) + 1 == idx   : indexOf(key) + idx;
		return idx;
	}

	@Override
	public void close()
	{
		clear();
	}

	public String status()
	{
		return ":" + size();
	}
}
