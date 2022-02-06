package org.sqlbuilder.pb;

import org.junit.Test;
import org.sqlbuilder.common.Names;

import static org.junit.Assert.assertEquals;

public class TestNames
{
	String[] tables = {"argns", "args", "aspects", "examples", "forms", "funcs", "persons", "rels", "roles", "members", "rolesets", "tenses", "vnthetas", "voices", "words", "pbrolesets_fnframes", "pbrolesets_vnclasses", "pbroles_vnroles"};

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
			assertEquals("pb_" + key, t);
		}
	}
}
