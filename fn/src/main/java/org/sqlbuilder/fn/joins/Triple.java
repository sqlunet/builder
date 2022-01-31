package org.sqlbuilder.fn.joins;

import java.util.Objects;

public class Triple<T, U, V>
{
	protected final T first;

	protected final U second;

	protected final V third;

	public Triple(final T first, final U second, final V third)
	{
		this.first = first;
		this.second = second;
		this.third = third;
	}

	public T getFirst()
	{
		return first;
	}

	public U getSecond()
	{
		return second;
	}

	public V getThird()
	{
		return third;
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
		Triple<?, ?, ?> that = (Triple<?, ?, ?>) o;
		return Objects.equals(first, that.first) && Objects.equals(second, that.second) && Objects.equals(third, that.third);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(first, second, third);
	}

	@Override
	public String toString()
	{
		return String.format("[VU-AS first=%s second=%s third=%s]", first, second, third);
	}
}
