package org.sqlbuilder.fn;

import org.junit.Test;

import java.util.Comparator;
import java.util.Set;

public class TestCollector
{
	private static final Set<String> SET = Set.of("one", "two", "three");

	private static final Comparator<String> COMPARATOR = Comparator.naturalOrder();

	private static final SetCollector<String> C = new SetCollector<>(COMPARATOR);

	private static final SetCollector<String> D = new SetCollector<>(COMPARATOR);

	@Test
	public void test()
	{
		for (var e : SET)
		{
			C.add(e);
		}

		System.err.println("[BEFORE]" + C.status());
		try (var ignored = C.open())
		{
			System.err.println("[ACTIVE]" + C.status());
			System.out.println(C.get("one"));
			System.out.println(C.get("two"));
			System.out.println(C.get("three"));
			System.out.println(C.get("four"));
		}
		System.err.println("[AFTER]" + C.status());
		System.out.println(C.get("one"));
		System.out.println(C.get("two"));
		System.out.println(C.get("three"));
		System.out.println(C.get("four"));
	}

	@Test
	public void test2()
	{
		for (var e : SET)
		{
			C.add(e);
			D.add(e);
		}

		System.err.println("[BEFORE]" + C.status());
		try (var ignored = C.open(); var ignored2 = D.open())
		{
			System.err.println("[ACTIVE]" + C.status() + D.status());
			System.out.println("c " + C.get("one"));
			System.out.println("c " + C.get("two"));
			System.out.println("c " + C.get("three"));
			System.out.println("c " + C.get("four"));
			System.out.println("d " + D.get("one"));
			System.out.println("d " + D.get("two"));
			System.out.println("d " + D.get("three"));
			System.out.println("d " + D.get("four"));
		}
		System.err.println("[AFTER]" + C.status());
		System.out.println("c " + C.get("one"));
		System.out.println("c " + C.get("two"));
		System.out.println("c " + C.get("three"));
		System.out.println("c " + C.get("four"));
		System.out.println("d " + D.get("one"));
		System.out.println("d " + D.get("two"));
		System.out.println("d " + D.get("three"));
		System.out.println("d " + D.get("four"));
	}
}
