package org.sqlbuilder2.ser;

import java.io.Serializable;
import java.util.Objects;

/**
 * Triplet
 *
 * @param <T> type of first
 * @param <U> type of second
 * @param <V> type of third
 * @param <W> type of fourth
 */
public class Quartet<T, U, V, W> implements Serializable
{
	public final T first;
	public final U second;
	public final V third;
	public final W fourth;

	/**
	 * Constructor
	 *
	 * @param first  first
	 * @param second second
	 * @param third  third
	 * @param fourth fourth
	 */
	public Quartet(final T first, final U second, final V third, final W fourth)
	{
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth = fourth;
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
		Quartet<?, ?, ?, ?> that = (Quartet<?, ?, ?, ?>) o;
		return first.equals(that.first) && second.equals(that.second) && third.equals(that.third) && third.equals(that.fourth);
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

	public W getFourth()
	{
		return fourth;
	}

	@Override
	public String toString()
	{
		return String.format("(%s,%s,%s,%s)", first, second, third, fourth);
	}
}
