package org.sqlbuilder2.legacy;

import java.io.Serializable;
import java.util.Objects;

/**
 * Pair
 *
 * @param <T> type of first
 * @param <U> type of second
 */
public class Pair<T, U, V> implements Serializable
{
	public final T first;
	public final U second;

	/**
	 * Constructor
	 *
	 * @param first  first
	 * @param second second
	 */
	public Pair(final T first, final U second)
	{
		this.first = first;
		this.second = second;
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
		Pair<?, ?, ?> triplet = (Pair<?, ?, ?>) o;
		return first.equals(triplet.first) && second.equals(triplet.second);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(first, second);
	}
}
