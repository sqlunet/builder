package org.sqlbuilder.vn;

import org.sqlbuilder.common.Insert;
import org.sqlbuilder.common.ProvidesIdTo;
import org.sqlbuilder.vn.joins.VnFrameExampleMapping;
import org.sqlbuilder.vn.joins.VnGroupingMapping;
import org.sqlbuilder.vn.joins.VnPredicateMapping;
import org.sqlbuilder.vn.objects.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Properties;

public class Inserter
{
	private final File outDir;

	public Inserter(final Properties conf)
	{
		this.outDir = new File(conf.getProperty("vnoutdir", "sql/data"));
		if (!this.outDir.exists())
		{
			this.outDir.mkdirs();
		}
	}

	//TODO external word map
	public void insert() throws FileNotFoundException
	{
		try ( //
		      @ProvidesIdTo(type = VnClass.class) var ignored1 = VnClass.COLLECTOR.open(); //
		      @ProvidesIdTo(type = VnGrouping.class) var ignored2 = VnGrouping.COLLECTOR.open(); //
		      @ProvidesIdTo(type = VnRoleType.class) var ignored3 = VnRoleType.COLLECTOR.open(); //
		      @ProvidesIdTo(type = VnGrouping.class) var ignored4 = VnRole.COLLECTOR.open(); //
		      @ProvidesIdTo(type = VnRestrType.class) var ignored5 = VnRestrType.COLLECTOR.open(); //
		      @ProvidesIdTo(type = VnRestrs.class) var ignored6 = VnRestrs.COLLECTOR.open(); //
		      @ProvidesIdTo(type = VnFrame.class) var ignored7 = VnFrame.COLLECTOR.open(); //
		      @ProvidesIdTo(type = VnFrameName.class) var ignored8 = VnFrameName.COLLECTOR.open(); //
		      @ProvidesIdTo(type = VnFrameSubName.class) var ignored9 = VnFrameSubName.COLLECTOR.open(); //
		      @ProvidesIdTo(type = VnFrameExample.class) var ignored10 = VnFrameExample.COLLECTOR.open(); //
		      @ProvidesIdTo(type = VnFrameExample.class) var ignored11 = VnSyntax.COLLECTOR.open(); //
		      @ProvidesIdTo(type = VnFrameExample.class) var ignored12 = VnSemantics.COLLECTOR.open(); //
		      @ProvidesIdTo(type = VnPredicate.class) var ignored13 = VnPredicate.COLLECTOR.open(); //
		      @ProvidesIdTo(type = VnWord.class) var ignored14 = VnWord.COLLECTOR.open() //
		)
		{
			// from pass1
			Insert.insert(VnClass.COLLECTOR, new File(outDir, Names.CLASSES.FILE), Names.CLASSES.TABLE, Names.CLASSES.COLUMNS);
			Insert.insert(VnGrouping.COLLECTOR, new File(outDir, Names.GROUPINGS.FILE), Names.GROUPINGS.TABLE, Names.GROUPINGS.COLUMNS);
			Insert.insert(VnRoleType.COLLECTOR, new File(outDir, Names.ROLETYPES.FILE), Names.ROLETYPES.TABLE, Names.ROLETYPES.COLUMNS);
			Insert.insert(VnRestrType.COLLECTOR, new File(outDir, Names.RESTRTYPES.FILE), Names.RESTRTYPES.TABLE, Names.RESTRTYPES.COLUMNS);
			Insert.insert(VnRestrs.COLLECTOR, new File(outDir, Names.RESTRS.FILE), Names.RESTRS.TABLE, Names.RESTRS.COLUMNS);

			Insert.insert(VnFrameName.COLLECTOR, new File(outDir, Names.FRAMENAMES.FILE), Names.FRAMENAMES.TABLE, Names.FRAMENAMES.COLUMNS);
			Insert.insert(VnFrameSubName.COLLECTOR, new File(outDir, Names.FRAMESUBNAMES.FILE), Names.FRAMESUBNAMES.TABLE, Names.FRAMESUBNAMES.COLUMNS);
			Insert.insert(VnFrameExample.COLLECTOR, new File(outDir, Names.EXAMPLES.FILE), Names.EXAMPLES.TABLE, Names.EXAMPLES.COLUMNS);
			Insert.insert(VnSyntax.COLLECTOR, new File(outDir, Names.SYNTAXES.FILE), Names.SYNTAXES.TABLE, Names.SYNTAXES.COLUMNS);
			Insert.insert(VnSemantics.COLLECTOR, new File(outDir, Names.SEMANTICS.FILE), Names.SEMANTICS.TABLE, Names.SEMANTICS.COLUMNS);
			Insert.insert(VnPredicate.COLLECTOR, new File(outDir, Names.PREDICATES.FILE), Names.PREDICATES.TABLE, Names.PREDICATES.COLUMNS);

			// from pass2
			Insert.insert(VnRole.COLLECTOR, new File(outDir, Names.ROLES.FILE), Names.ROLES.TABLE, Names.ROLES.COLUMNS);
			Insert.insert(VnFrame.COLLECTOR, new File(outDir, Names.FRAMES.FILE), Names.FRAMES.TABLE, Names.FRAMES.COLUMNS);

			// from pass3
			Insert.insert(VnFrameExampleMapping.SET, null, new File(outDir, Names.FRAMES_EXAMPLES.FILE), Names.FRAMES_EXAMPLES.TABLE, Names.FRAMES_EXAMPLES.COLUMNS);
			Insert.insert(VnPredicateMapping.SET, null, new File(outDir, Names.SEMANTICS_PREDICATES.FILE), Names.SEMANTICS_PREDICATES.TABLE, Names.SEMANTICS_PREDICATES.COLUMNS);

			// from pass4
			Insert.insert(VnMember.SET, null, new File(outDir, Names.MEMBERS.FILE), Names.MEMBERS.TABLE, Names.MEMBERS.COLUMNS);
			Insert.insert(VnGroupingMapping.SET, null, new File(outDir, Names.MEMBERS_GROUPINGS.FILE), Names.MEMBERS_GROUPINGS.TABLE, Names.MEMBERS_GROUPINGS.COLUMNS);

			Insert.insert(VnWord.COLLECTOR, new File(outDir, Names.WORDS.FILE), Names.WORDS.TABLE, Names.WORDS.COLUMNS);
			Insert.insert(VnMemberSense.SET, null, new File(outDir, Names.MEMBERS_SENSES.FILE), Names.MEMBERS_SENSES.TABLE, Names.MEMBERS_SENSES.COLUMNS);
		}
	}
}
