package org.sqlbuilder.fn;

import org.junit.Test;
import org.sqlbuilder.common.Names;

import static org.junit.Assert.assertEquals;

public class TestNames
{
	String[] tables = {"annosets", "coretypes", "corpuses", "cxns", "documents", "fes", "fetypes", "fes_semtypes", "fes_excluded", "fes_required", "ferealizations", "ferealizations_valenceunits", "fegrouprealizations", "fes_fegrouprealizations", "frames", "frames_related", "frames_related", "framerelations", "frames_semtypes", "gftypes", "governors", "governors_annosets", "grouppatterns", "grouppatterns_patterns", "grouppatterns_annosets", "labelitypes", "labels", "labeltypes", "layers", "layertypes", "lexemes", "lexunits", "lexunits_governors", "lexunits_semtypes", "poses", "pttypes", "semtypes", "semtypes_supers", "sentences", "statuses", "subcorpuses", "subcorpuses_sentences", "valenceunits", "valenceunits_annosets", "words"};

	@Test
	public void testNames()
	{
		Names names = new Names("fn");
		for (var key : tables)
		{
			var f = names.file(key);
			var t = names.table(key);
			var c = names.columns(key);
			System.out.printf("%s - %s %s %s%n", key, f, t, c);
			assertEquals(key + ".sql", f);
			assertEquals("fn_" + key, t);
		}
	}
}
