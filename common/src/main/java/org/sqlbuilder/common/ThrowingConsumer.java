package org.sqlbuilder.common;

import java.util.function.Consumer;

@FunctionalInterface
public interface ThrowingConsumer<T> extends Consumer<T>
{
	@Override
	default void accept(T t)
	{
		acceptThrows(t);
	}

	void acceptThrows(T t);
}
