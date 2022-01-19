package org.sqlbuilder.vn;

import org.sqlbuilder.common.Insert;
import org.sqlbuilder.common.MapFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Properties;

public class Inserter
{
	private final File outDir;

	public Inserter(final Properties conf)
	{
		this.outDir = new File(conf.getProperty("outdir", "vn"));
		if (!this.outDir.exists())
		{
			this.outDir.mkdirs();
		}
	}

	private static void makeMaps()
	{
		// from pass1
		VnClass.MAP = MapFactory.makeSortedMap(VnClass.SET);
		VnGrouping.MAP = MapFactory.makeSortedMap(VnGrouping.SET);
		VnRoleType.MAP = MapFactory.makeSortedMap(VnRoleType.SET);
		VnRestrs.MAP = MapFactory.makeSortedMap(VnRestrs.SET);
		VnRestrType.MAP = MapFactory.makeSortedMap(VnRestrType.SET);
		VnFrameName.MAP = MapFactory.makeSortedMap(VnFrameName.SET);
		VnFrameSubName.MAP = MapFactory.makeSortedMap(VnFrameSubName.SET);
		VnFrameExample.MAP = MapFactory.makeSortedMap(VnFrameExample.SET);
		VnSyntax.MAP = MapFactory.makeSortedMap(VnSyntax.SET);
		VnSemantics.MAP = MapFactory.makeSortedMap(VnSemantics.SET);
		VnPredicate.MAP = MapFactory.makeSortedMap(VnPredicate.SET);

		// from pass2
		VnRole.MAP = MapFactory.makeSortedMap(VnRole.SET);
		VnFrame.MAP = MapFactory.makeSortedMap(VnFrame.SET);

		//TODO external map
		VnWord.MAP = MapFactory.makeSortedMap(VnWord.SET);
	}

	public void insert() throws FileNotFoundException
	{
		makeMaps();

		// from pass1
		Insert.insert(VnClass.MAP, new File(outDir, Names.CLASSES.FILE), Names.CLASSES.TABLE, Names.CLASSES.COLUMNS);
		Insert.insert(VnGrouping.MAP, new File(outDir, Names.GROUPINGS.FILE), Names.GROUPINGS.TABLE, Names.GROUPINGS.COLUMNS);
		Insert.insert(VnRoleType.MAP, new File(outDir, Names.ROLETYPES.FILE), Names.ROLETYPES.TABLE, Names.ROLETYPES.COLUMNS);
		Insert.insert(VnRestrType.MAP, new File(outDir, Names.RESTRTYPES.FILE), Names.RESTRTYPES.TABLE, Names.RESTRTYPES.COLUMNS);
		Insert.insert(VnRestrs.MAP, new File(outDir, Names.RESTRS.FILE), Names.RESTRS.TABLE, Names.RESTRS.COLUMNS);

		Insert.insert(VnFrameName.MAP, new File(outDir, Names.FRAMENAMES.FILE), Names.FRAMENAMES.TABLE, Names.FRAMENAMES.COLUMNS);
		Insert.insert(VnFrameSubName.MAP, new File(outDir, Names.FRAMESUBNAMES.FILE), Names.FRAMESUBNAMES.TABLE, Names.FRAMESUBNAMES.COLUMNS);
		Insert.insert(VnFrameExample.MAP, new File(outDir, Names.EXAMPLES.FILE), Names.EXAMPLES.TABLE, Names.EXAMPLES.COLUMNS);
		Insert.insert(VnSyntax.MAP, new File(outDir, Names.SYNTAXES.FILE), Names.SYNTAXES.TABLE, Names.SYNTAXES.COLUMNS);
		Insert.insert(VnSemantics.MAP, new File(outDir, Names.SEMANTICS.FILE), Names.SEMANTICS.TABLE, Names.SEMANTICS.COLUMNS);
		Insert.insert(VnPredicate.MAP, new File(outDir, Names.PREDICATES.FILE), Names.PREDICATES.TABLE, Names.PREDICATES.COLUMNS);

		// from pass2
		Insert.insert(VnRole.MAP, new File(outDir, Names.ROLES.FILE), Names.ROLES.TABLE, Names.ROLES.COLUMNS);
		Insert.insert(VnFrame.MAP, new File(outDir, Names.FRAMES.FILE), Names.FRAMES.TABLE, Names.FRAMES.COLUMNS);

		// from pass3
		Insert.insert(VnFrameExampleMapping.SET, null, new File(outDir, Names.FRAMES_EXAMPLES.FILE), Names.FRAMES_EXAMPLES.TABLE, Names.FRAMES_EXAMPLES.COLUMNS);
		Insert.insert(VnPredicateMapping.SET, null, new File(outDir, Names.SEMANTICS_PREDICATES.FILE), Names.SEMANTICS_PREDICATES.TABLE, Names.SEMANTICS_PREDICATES.COLUMNS);

		// from pass4
		Insert.insert(VnMember.SET, null, new File(outDir, Names.MEMBERS.FILE), Names.MEMBERS.TABLE, Names.MEMBERS.COLUMNS);
		Insert.insert(VnGroupingMapping.SET, null, new File(outDir, Names.MEMBERS_GROUPINGS.FILE), Names.MEMBERS_GROUPINGS.TABLE, Names.MEMBERS_GROUPINGS.COLUMNS);

		Insert.insert(VnWord.MAP, new File(outDir, Names.WORDS.FILE), Names.WORDS.TABLE, Names.WORDS.COLUMNS);
		Insert.insert(VnMemberSense.SET, null, new File(outDir, Names.MEMBERS_SENSES.FILE), Names.MEMBERS_SENSES.TABLE, Names.MEMBERS_SENSES.COLUMNS);
	}
}
