package org.sqlbuilder2.legacy;

import java.io.Serializable;
import java.util.Objects;

/**
 * Triplet
 *
 * @param <T> type of first
 * @param <U> type of second
 * @param <V> type of third
 */
public class Triplet<T, U, V> implements Serializable
{
	public final T first;
	public final U second;
	public final V third;

	/**
	 * Constructor
	 *
	 * @param first  first
	 * @param second second
	 * @param third  third
	 */
	public Triplet(final T first, final U second, final V third)
	{
		this.first = first;
		this.second = second;
		this.third = third;
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
		Triplet<?, ?, ?> triplet = (Triplet<?, ?, ?>) o;
		return first.equals(triplet.first) && second.equals(triplet.second) && third.equals(triplet.third);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(first, second, third);
	}
}
