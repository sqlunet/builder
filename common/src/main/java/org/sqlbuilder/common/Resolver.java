package org.sqlbuilder.common;

import java.util.Map;
import java.util.function.Function;

public abstract class Resolver<T, R> implements Function<T, R>
{
	public final Map<T, R> map;

	public Resolver(final Map<T, R> map)
	{
		this.map = map;
	}

	@Nullable
	@Override
	public R apply(final T k)
	{
		return map.get(k);
	}
}
