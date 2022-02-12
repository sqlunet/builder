package org.sqlbuilder.common;

import java.util.Map;
import java.util.function.Function;

public abstract class Resolver<T> implements Function<String, T>
{
	public final Map<String, T> map;

	public Resolver(final Map<String, T> map)
	{
		this.map = map;
	}

	@Nullable
	@Override
	public T apply(final String k)
	{
		return map.get(k);
	}
}
