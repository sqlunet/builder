package org.sqlbuilder2.ser;

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
		Triplet<?, ?, ?> that = (Triplet<?, ?, ?>) o;
		return first.equals(that.first) && second.equals(that.second) && third.equals(that.third);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(first, second, third);
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
	public String toString()
	{
		return String.format("(%s,%s,%s)", first, second, third);
	}
}
