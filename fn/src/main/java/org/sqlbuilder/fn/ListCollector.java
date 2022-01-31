package org.sqlbuilder.fn;

import java.io.Closeable;
import java.util.ArrayList;

public class ListCollector<T> extends ArrayList<T> implements Closeable
{
	private boolean isOpen = false;

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
		return super.add(item);
	}

	public Integer get(final Object key)
	{
		return super.indexOf(key) + 1;
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
