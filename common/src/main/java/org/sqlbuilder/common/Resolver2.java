package org.sqlbuilder.common;

import java.util.Map;
import java.util.function.BiFunction;

public abstract class Resolver2<T, U, R> implements BiFunction<T, U, R>
{
	public final Map<T, Map<U, R>> map;

	public Resolver2(final Map<T, Map<U, R>> map)
	{
		this.map = map;
	}

	@Nullable
	@Override
	public R apply(final T k, U k2)
	{
		var m2 = map.get(k);
		if (m2 == null)
		{
			return null;
		}
		return m2.get(k2);
	}
}
