package org.sqlbuilder.common;

public interface Resolvable<T, R> extends Insertable
{
	default R resolve(final Resolver<T, R> resolver)
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