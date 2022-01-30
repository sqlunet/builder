package org.sqlbuilder.fn.joins;

import java.util.Comparator;
import java.util.Objects;

public class Pair<T, U>
{
	protected final T first;

	protected final U second;

	public Pair(final T first, final U second)
	{
		this.first = first;
		this.second = second;
	}

	public T getFirst()
	{
		return first;
	}

	public U getSecond()
	{
		return second;
	}

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		Pair<?, ?> pair = (Pair<?, ?>) o;
		return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(first, second);
	}

	@Override
	public String toString()
	{
		return String.format("[VU-AS first=%s second=%s]", first, second);
	}
}
