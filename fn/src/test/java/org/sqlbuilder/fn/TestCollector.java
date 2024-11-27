package org.sqlbuilder.fn;

import org.junit.Test;
import org.sqlbuilder.common.SetCollector;

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
			System.out.println(C.apply("one"));
			System.out.println(C.apply("two"));
			System.out.println(C.apply("three"));
			System.out.println(C.apply("four"));
		}
		System.err.println("[AFTER]" + C.status());
		try { System.out.println(C.apply("one")); } catch (IllegalStateException ise) {System.out.println(ise.getMessage());}
		try { System.out.println(C.apply("two")); } catch (IllegalStateException ise) {System.out.println(ise.getMessage());}
		try { System.out.println(C.apply("three")); } catch (IllegalStateException ise) {System.out.println(ise.getMessage());}
		try { System.out.println(C.apply("four")); } catch (IllegalStateException ise) {System.out.println(ise.getMessage());}
	}

	@Test(expected=IllegalStateException.class)
	public void testFail()
	{
		for (var e : SET)
		{
			C.add(e);
		}

		System.err.println("[BEFORE]" + C.status());
		try (var ignored = C.open())
		{
			System.err.println("[ACTIVE]" + C.status());
			System.out.println(C.apply("one"));
			System.out.println(C.apply("two"));
			System.out.println(C.apply("three"));
			System.out.println(C.apply("four"));
		}
		System.err.println("[AFTER]" + C.status());
		System.out.println(C.apply("one"));
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
			System.out.println("c " + C.apply("one"));
			System.out.println("c " + C.apply("two"));
			System.out.println("c " + C.apply("three"));
			System.out.println("c " + C.apply("four"));
			System.out.println("d " + D.apply("one"));
			System.out.println("d " + D.apply("two"));
			System.out.println("d " + D.apply("three"));
			System.out.println("d " + D.apply("four"));
		}
		System.err.println("[AFTER]" + C.status());
		try { System.out.println("c " + C.apply("one")); } catch (IllegalStateException ise) {System.out.println(ise.getMessage());}
		try { System.out.println("c " + C.apply("two")); } catch (IllegalStateException ise) {System.out.println(ise.getMessage());}
		try { System.out.println("c " + C.apply("three")); } catch (IllegalStateException ise) {System.out.println(ise.getMessage());}
		try { System.out.println("c " + C.apply("four")); } catch (IllegalStateException ise) {System.out.println(ise.getMessage());}
		try { System.out.println("d " + D.apply("one")); } catch (IllegalStateException ise) {System.out.println(ise.getMessage());}
		try { System.out.println("d " + D.apply("two")); } catch (IllegalStateException ise) {System.out.println(ise.getMessage());}
		try { System.out.println("d " + D.apply("three")); } catch (IllegalStateException ise) {System.out.println(ise.getMessage());}
		try { System.out.println("d " + D.apply("four")); } catch (IllegalStateException ise) {System.out.println(ise.getMessage());}
	}

	@Test(expected=IllegalStateException.class)
	public void test2Fail()
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
			System.out.println("c " + C.apply("one"));
			System.out.println("c " + C.apply("two"));
			System.out.println("c " + C.apply("three"));
			System.out.println("c " + C.apply("four"));
			System.out.println("d " + D.apply("one"));
			System.out.println("d " + D.apply("two"));
			System.out.println("d " + D.apply("three"));
			System.out.println("d " + D.apply("four"));
		}
		System.err.println("[AFTER]" + C.status());
		System.out.println("c " + C.apply("one"));
	}
}
