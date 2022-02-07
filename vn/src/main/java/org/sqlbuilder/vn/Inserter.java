package org.sqlbuilder.vn;

import org.sqlbuilder.common.Insert;
import org.sqlbuilder.common.Names;
import org.sqlbuilder.common.ProvidesIdTo;
import org.sqlbuilder.vn.joins.*;
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
			Insert.insert(VnClass.COLLECTOR, new File(outDir, Names.file("classes")), Names.table("classes"), Names.columns("classes"));
			Insert.insert(Grouping.COLLECTOR, new File(outDir, Names.file("groupings")), Names.table("groupings"), Names.columns("groupings"));
			Insert.insert(RoleType.COLLECTOR, new File(outDir, Names.file("roletypes")), Names.table("roletypes"), Names.columns("roletypes"));
			Insert.insert(RestrType.COLLECTOR, new File(outDir, Names.file("restrtypes")), Names.table("restrtypes"), Names.columns("restrtypes"));
			Insert.insert(Restrs.COLLECTOR, new File(outDir, Names.file("restrs")), Names.table("restrs"), Names.columns("restrs"));

			Insert.insert(FrameName.COLLECTOR, new File(outDir, Names.file("framenames")), Names.table("framenames"), Names.columns("framenames"));
			Insert.insert(FrameSubName.COLLECTOR, new File(outDir, Names.file("framesubnames")), Names.table("framesubnames"), Names.columns("framesubnames"));
			Insert.insert(FrameExample.COLLECTOR, new File(outDir, Names.file("examples")), Names.table("examples"), Names.columns("examples"));
			Insert.insert(Syntax.COLLECTOR, new File(outDir, Names.file("syntaxes")), Names.table("syntaxes"), Names.columns("syntaxes"));
			Insert.insert(Semantics.COLLECTOR, new File(outDir, Names.file("semantics")), Names.table("semantics"), Names.columns("semantics"));
			Insert.insert(Predicate.COLLECTOR, new File(outDir, Names.file("predicates")), Names.table("predicates"), Names.columns("predicates"));

			// from pass2
			Insert.insert(Role.COLLECTOR, new File(outDir, Names.file("roles")), Names.table("roles"), Names.columns("roles"));
			Insert.insert(Frame.COLLECTOR, new File(outDir, Names.file("frames")), Names.table("frames"), Names.columns("frames"));

			// from pass3
			Insert.insert(Frame_Example.SET, null, new File(outDir, Names.file("frames_examples")), Names.table("frames_examples"), Names.columns("frames_examples"));
			Insert.insert(Predicate_Semantics.SET, null, new File(outDir, Names.file("predicates_semantics")), Names.table("predicates_semantics"), Names.columns("predicates_semantics"));

			// from pass4
			Insert.insert(VnWord.COLLECTOR, new File(outDir, Names.file("words")), Names.table("words"), Names.columns("words"));

			Insert.insert(Class_Word.SET, null, new File(outDir, Names.file("members")), Names.table("members"), Names.columns("members"));
			Insert.insert(Class_Role.SET, null, new File(outDir, Names.file("classes_roles")), Names.table("classes_roles"), Names.columns("classes_roles"));
			Insert.insert(Class_Frame.SET, null, new File(outDir, Names.file("classes_frames")), Names.table("classes_frames"), Names.columns("classes_frames"));
			Insert.insert(Member_Grouping.SET, null, new File(outDir, Names.file("members_groupings")), Names.table("members_groupings"), Names.columns("members_groupings"));
			Insert.insert(Member_Sense.SET, null, new File(outDir, Names.file("members_senses")), Names.table("members_senses"), Names.columns("members_senses"));
		}
	}
}
