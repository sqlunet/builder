package org.sqlbuilder.pb;

import org.junit.Test;
import org.sqlbuilder.common.SetCollector;
import org.sqlbuilder.pb.collectors.PbCollector;
import org.sqlbuilder.pb.foreign.AliasFnFeLinks;
import org.sqlbuilder.pb.foreign.AliasVnRoleLinks;
import org.sqlbuilder.pb.objects.*;

import java.io.File;
import java.util.Properties;

public class TestParseFile
{
	@Test
	public void testParse()
	{
		String path = System.getenv("PARSE");
		System.out.println(path);
		File file = new File(path);
		Properties props = org.sqlbuilder.common.Module.getProperties("pb.properties");
		assert props != null;
		new PbCollector(props).processPropBankFile(file.getAbsolutePath(), file.getName());

		var aspects = Example.ASPECT_COLLECTOR;
		var forms = Example.FORM_COLLECTOR;
		var persons = Example.PERSON_COLLECTOR;
		var tenses = Example.TENSE_COLLECTOR;
		var voices = Example.VOICE_COLLECTOR;
		var funcs = Func.COLLECTOR;
		var vnLinks = AliasVnRoleLinks.COLLECTOR;
		var fnLinks = AliasFnFeLinks.COLLECTOR;
		var rolesets = RoleSet.COLLECTOR;
		var roles = Role.COLLECTOR;
		var examples = Example.COLLECTOR;
		var rels = Rel.COLLECTOR;
		var args = Arg.COLLECTOR;

		var words = Word.COLLECTOR;

		var names = new String[]{"aspects", "forms", "persons", "tenses", "voices", "funcs", "vnlinks", "fnlinks", "rolesets", "roles", "examples", "rels", "args", "words",};
		var collectors = new SetCollector[]{aspects, forms, persons, tenses, voices, funcs, vnLinks, fnLinks, rolesets, roles, examples, rels, args, words,};
		var i = 0;
		for (var c : collectors)
		{
			System.out.println(names[i] + " " + c.size());
			i++;
		}
	}
}
