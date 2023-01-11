package org.sqlbuilder2.legacy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sqlbuilder2.ser.Triplet;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SensekeysTest
{
	private static Map<Triplet<String, Character, Integer>, String> map;

	@BeforeAll
	public static void init() throws IOException
	{
		map = SenseToSensekeyProcessor.getLemmaPosOffsetToSensekey(new File("data/YY/index.sense"));
		System.out.println(map);
	}

	@Test
	public void testTripletsMapInt()
	{
		Triplet<String, Character, Integer> t = new Triplet<>("baby", 'n', 796767);
		String sk = map.get(t);
		assertNotEquals(null, sk);
		System.out.println(sk);
	}

	@Test
	public void testTripletsMapLong()
	{
		Triplet<String, Character, Long> t = new Triplet<>("baby", 'n', 796767L);
		String sk = map.get(t);
		assertEquals(null, sk);
	}

	@Test
	public void testTripletsLongEq()
	{
		Triplet<String, Character, Long> t1 = new Triplet<>("baby", 'n', 796767L);
		Triplet<String, Character, Long> t2 = new Triplet<>("baby", 'n', 796767L);
		assertEquals(t1, t2);
	}

	@Test
	public void testTripletsIntEq()
	{
		Triplet<String, Character, Integer> t1 = new Triplet<>("baby", 'n', 796767);
		Triplet<String, Character, Integer> t2 = new Triplet<>("baby", 'n', 796767);
		assertEquals(t1, t2);
	}

	@Test
	public void testTripletsLongIntEq()
	{
		Triplet<String, Character, Integer> t1 = new Triplet<>("baby", 'n', 796767);
		Triplet<String, Character, Long> t2 = new Triplet<>("baby", 'n', 7967678L);
		Assertions.assertThrows(AssertionError.class, () -> {
			assertEquals(t1, t2);
		});
	}

	@Test
	public void testTripletsLong()
	{
		Triplet<String, Character, Long> t1 = new Triplet<>("baby", 'n', 796767L);
		Triplet<String, Character, Long> t2 = new Triplet<>("baby", 'n', 796768L);
		Assertions.assertThrows(AssertionError.class, () -> {
			assertEquals(t1, t2);
		});
	}

	@Test
	public void testTripletsInt()
	{
		Triplet<String, Character, Integer> t1 = new Triplet<>("baby", 'n', 796767);
		Triplet<String, Character, Integer> t2 = new Triplet<>("baby", 'n', 796768);
		Assertions.assertThrows(AssertionError.class, () -> {
			assertEquals(t1, t2);
		});
	}

	@Test
	public void testTripletsLongInt()
	{
		Triplet<String, Character, Integer> t1 = new Triplet<>("baby", 'n', 796767);
		Triplet<String, Character, Long> t2 = new Triplet<>("baby", 'n', 796768L);
		Assertions.assertThrows(AssertionError.class, () -> {
			assertEquals(t1, t2);
		});
	}
}
