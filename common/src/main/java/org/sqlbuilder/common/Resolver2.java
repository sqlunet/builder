package org.sqlbuilder.common;

import org.sqlbuilder2.ser.Pair;

import java.util.function.Function;

public class Resolver2<T, R, U, S> implements Function<Pair<T,U>,Pair<R,S>>
{
	private final Function<T, R> r1;

	private final Function<U, S> r2;

	public Resolver2(final Function<T, R> r1, final Function<U, S> r2)
	{
		this.r1 = r1;
		this.r2 = r2;
	}

	@Override
	public Pair<R, S> apply(final Pair<T, U> in)
	{
		return new Pair<>(r1.apply(in.first), r2.apply(in.second));
	}
}
