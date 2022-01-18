package org.sqlbuilder.fn;

public class Pair<T, U>
{
	protected final T first;

	protected final U second;

	public Pair(final T first, final U second)
	{
		this.first = first;
		this.second = second;
	}

	@Override
	public String toString()
	{
		return String.format("[VU-AS first=%s second=%s]", this.first, this.second);
	}
}
