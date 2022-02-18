package org.sqlbuilder.common;

import java.util.function.Function;

public interface Resolvable<T, R> extends Insertable
{
	default R resolve(final Function<T, R> resolver)
	{
		T resolving = resolving();
		if (resolving == null)
		{
			return null;
		}
		return resolver.apply(resolving);
	}

	T resolving();
}
