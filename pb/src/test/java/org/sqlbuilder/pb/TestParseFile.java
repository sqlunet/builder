package org.sqlbuilder.pb;

import org.junit.Test;
import org.sqlbuilder.common.SetCollector2;
import org.sqlbuilder.pb.collectors.PbCollector;
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
		var thetas = Theta.COLLECTOR;

		var rolesets = RoleSet.COLLECTOR;
		var roles = Role.COLLECTOR;
		var examples = Example.COLLECTOR;
		var rels = Rel.COLLECTOR;
		var args = Arg.COLLECTOR;

		var words = Word.COLLECTOR;

		var names = new String[]{"aspects", "forms", "persons", "tenses", "voices", "funcs", "thetas", "rolesets", "roles", "examples", "rels", "args", "words",};
		var collectors = new SetCollector2[]{aspects, forms, persons, tenses, voices, funcs, thetas, rolesets, roles, examples, rels, words,};
		var i = 0;
		for (var c : collectors)
		{
			System.out.println(names[i] + " " + c.getSize());
			i++;
		}
		var collectors2 = new SetCollector2[]{ args,};
		for (var c : collectors2)
		{
			System.out.println(names[i]);
			i++;
		}
	}
}
