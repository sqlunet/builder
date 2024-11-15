package org.sqlbuilder.pb;

import org.junit.Test;
import org.sqlbuilder.common.Names;

import static org.junit.Assert.assertEquals;

public class TestNames
{
	final String[] tables = {"argtypes", "args", "aspects", "examples", "forms", "funcs", "persons", "rels", "roles", "members", "rolesets", "tenses", "thetas", "voices", "words", "pbrolesets_fnframes", "pbrolesets_vnclasses", "pbroles_vnroles"};

	@Test
	public void testNames()
	{
		Names names = new Names("pb");
		for (var key : tables)
		{
			var f = names.file(key);
			var t = names.table(key);
			var c = names.columns(key);
			System.out.printf("%s - %s %s %s%n", key, f, t, c);
			assertEquals(key + ".sql", f);
			assertEquals("`pb_" + key + '`', t);
		}
	}
}
