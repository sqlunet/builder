package org.sqlbuilder.fn;

import org.junit.Test;

public class TestListCollector
{
	static class W<T>
	{
		final T wrapped;

		W(T wrapped)
		{
			this.wrapped = wrapped;
		}

		static <T> W<T> w(T wrapped)
		{
			return new W<>(wrapped);
		}

		@Override
		public String toString()
		{
			return wrapped.toString();
		}
	}

	@Test
	public void testListCollector()
	{
		ListCollector<W<String>> collector = new ListCollector<>();
		collector.add(W.w("one"));
		collector.add(W.w("two"));
		collector.add(W.w("two"));
		collector.add(W.w("one"));
		collector.add(W.w("three"));
		for (var item : collector)
		{
			// System.out.printf("%s%n", item);
			// System.out.printf("%s %d%n", item, collector.indexOf(item) + 1);
			System.out.printf("%s %d%n", item, collector.get(item));
		}
	}
}
