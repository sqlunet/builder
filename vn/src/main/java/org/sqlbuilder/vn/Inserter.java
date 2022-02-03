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
		      @ProvidesIdTo(type = Grouping.class) var ignored2 = Grouping.COLLECTOR.open(); //
		      @ProvidesIdTo(type = RoleType.class) var ignored3 = RoleType.COLLECTOR.open(); //
		      @ProvidesIdTo(type = Grouping.class) var ignored4 = Role.COLLECTOR.open(); //
		      @ProvidesIdTo(type = RestrType.class) var ignored5 = RestrType.COLLECTOR.open(); //
		      @ProvidesIdTo(type = Restrs.class) var ignored6 = Restrs.COLLECTOR.open(); //
		      @ProvidesIdTo(type = Frame.class) var ignored7 = Frame.COLLECTOR.open(); //
		      @ProvidesIdTo(type = FrameName.class) var ignored8 = FrameName.COLLECTOR.open(); //
		      @ProvidesIdTo(type = FrameSubName.class) var ignored9 = FrameSubName.COLLECTOR.open(); //
		      @ProvidesIdTo(type = FrameExample.class) var ignored10 = FrameExample.COLLECTOR.open(); //
		      @ProvidesIdTo(type = FrameExample.class) var ignored11 = Syntax.COLLECTOR.open(); //
		      @ProvidesIdTo(type = FrameExample.class) var ignored12 = Semantics.COLLECTOR.open(); //
		      @ProvidesIdTo(type = Predicate.class) var ignored13 = Predicate.COLLECTOR.open(); //
		      @ProvidesIdTo(type = VnWord.class) var ignored14 = VnWord.COLLECTOR.open() //
		)
		{
			// from pass1
			Insert.insert(VnClass.COLLECTOR, new File(outDir, Names.CLASSES.FILE), Names.CLASSES.TABLE, Names.CLASSES.COLUMNS);
			Insert.insert(Grouping.COLLECTOR, new File(outDir, Names.GROUPINGS.FILE), Names.GROUPINGS.TABLE, Names.GROUPINGS.COLUMNS);
			Insert.insert(RoleType.COLLECTOR, new File(outDir, Names.ROLETYPES.FILE), Names.ROLETYPES.TABLE, Names.ROLETYPES.COLUMNS);
			Insert.insert(RestrType.COLLECTOR, new File(outDir, Names.RESTRTYPES.FILE), Names.RESTRTYPES.TABLE, Names.RESTRTYPES.COLUMNS);
			Insert.insert(Restrs.COLLECTOR, new File(outDir, Names.RESTRS.FILE), Names.RESTRS.TABLE, Names.RESTRS.COLUMNS);

			Insert.insert(FrameName.COLLECTOR, new File(outDir, Names.FRAMENAMES.FILE), Names.FRAMENAMES.TABLE, Names.FRAMENAMES.COLUMNS);
			Insert.insert(FrameSubName.COLLECTOR, new File(outDir, Names.FRAMESUBNAMES.FILE), Names.FRAMESUBNAMES.TABLE, Names.FRAMESUBNAMES.COLUMNS);
			Insert.insert(FrameExample.COLLECTOR, new File(outDir, Names.EXAMPLES.FILE), Names.EXAMPLES.TABLE, Names.EXAMPLES.COLUMNS);
			Insert.insert(Syntax.COLLECTOR, new File(outDir, Names.SYNTAXES.FILE), Names.SYNTAXES.TABLE, Names.SYNTAXES.COLUMNS);
			Insert.insert(Semantics.COLLECTOR, new File(outDir, Names.SEMANTICS.FILE), Names.SEMANTICS.TABLE, Names.SEMANTICS.COLUMNS);
			Insert.insert(Predicate.COLLECTOR, new File(outDir, Names.PREDICATES.FILE), Names.PREDICATES.TABLE, Names.PREDICATES.COLUMNS);

			// from pass2
			Insert.insert(Role.COLLECTOR, new File(outDir, Names.ROLES.FILE), Names.ROLES.TABLE, Names.ROLES.COLUMNS);
			Insert.insert(Frame.COLLECTOR, new File(outDir, Names.FRAMES.FILE), Names.FRAMES.TABLE, Names.FRAMES.COLUMNS);

			// from pass3
			Insert.insert(VnFrameExampleMapping.SET, null, new File(outDir, Names.FRAMES_EXAMPLES.FILE), Names.FRAMES_EXAMPLES.TABLE, Names.FRAMES_EXAMPLES.COLUMNS);
			Insert.insert(VnPredicateMapping.SET, null, new File(outDir, Names.SEMANTICS_PREDICATES.FILE), Names.SEMANTICS_PREDICATES.TABLE, Names.SEMANTICS_PREDICATES.COLUMNS);

			// from pass4
			Insert.insert(ClassMember.SET, null, new File(outDir, Names.MEMBERS.FILE), Names.MEMBERS.TABLE, Names.MEMBERS.COLUMNS);
			Insert.insert(VnGroupingMapping.SET, null, new File(outDir, Names.MEMBERS_GROUPINGS.FILE), Names.MEMBERS_GROUPINGS.TABLE, Names.MEMBERS_GROUPINGS.COLUMNS);

			Insert.insert(VnWord.COLLECTOR, new File(outDir, Names.WORDS.FILE), Names.WORDS.TABLE, Names.WORDS.COLUMNS);
			Insert.insert(VnMemberSense.SET, null, new File(outDir, Names.MEMBERS_SENSES.FILE), Names.MEMBERS_SENSES.TABLE, Names.MEMBERS_SENSES.COLUMNS);
		}
	}
}
