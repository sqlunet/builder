package org.sqlbuilder.vn;

import org.junit.Test;
import org.sqlbuilder.common.Names;

import static org.junit.Assert.assertEquals;

public class TestNames
{
	String[] tables = {"classes", "members", "members_senses", "groupings", "members_groupings", "restrtypes", "restrs", "roletypes", "roles", "classes_roles", "classes_frames", "frames", "framenames", "framesubnames", "examples", "frames_examples", "semantics", "predicates", "predicates_semantics", "syntaxes", "words"};

	@Test
	public void testNames()
	{
		for (var key : tables)
		{
			var f = Names.file(key);
			var t = Names.table(key);
			var c = Names.columns(key);
			System.out.printf("%s - %s %s %s%n", key, f, t, c);
			assertEquals(key + ".sql", f);
			assertEquals("vn_" + key, t);
		}
	}
}
