package org.sqlbuilder.common;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ResourceBundle;

public class TestVariables
{
	@BeforeClass
	public static void init()
	{
		ResourceBundle bundle = ResourceBundle.getBundle("Names");
		Variables.set(bundle);
	}

	private static final String[] dollarVs = new String[]{"${var.a}", "${var.b}", "${var.c}", "__${var.a}__", "__${var.b}__", "__${var.c}__", "${var.z}"};

	private static final String[] atVs = new String[]{"@{var.a}", "@{var.b}", "@{var.c}", "__@{var.a}__", "__@{var.b}__", "__@{var.c}__", "@{var.z}"};;

	@Test
	public void testVariablesWithDollar()
	{
		testVariables(dollarVs);
	}

	@Test
	public void testVariablesWithAt()
	{
		testVariables(atVs);
	}

	public void testVariables(String[] vs)
	{
		for (var v : vs)
		{
			try
			{
				var w = Variables.varSubstitution(v);
				System.out.println(w);
			}
			catch (IllegalArgumentException iae)
			{
				System.err.println("Not found; " + v);
			}
		}
	}
}
