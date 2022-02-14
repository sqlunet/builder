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
	protected final Names names;

	protected File outDir;

	protected boolean resolve = false;

	public Inserter(final Properties conf)
	{
		this.names = new Names("vn");
		this.outDir = new File(conf.getProperty("vnoutdir", "sql/data"));
		if (!this.outDir.exists())
		{
			this.outDir.mkdirs();
		}
	}

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
		      @ProvidesIdTo(type = Word.class) var ignored14 = Word.COLLECTOR.open() //
		)
		{
			Insert.insert(VnClass.COLLECTOR, new File(outDir, names.file("classes")), names.table("classes"), names.columns("classes"));
			Insert.insert(Grouping.COLLECTOR, new File(outDir, names.file("groupings")), names.table("groupings"), names.columns("groupings"));
			Insert.insert(RoleType.COLLECTOR, new File(outDir, names.file("roletypes")), names.table("roletypes"), names.columns("roletypes"));
			Insert.insert(RestrType.COLLECTOR, new File(outDir, names.file("restrtypes")), names.table("restrtypes"), names.columns("restrtypes"));
			Insert.insert(Restrs.COLLECTOR, new File(outDir, names.file("restrs")), names.table("restrs"), names.columns("restrs"));

			Insert.insert(FrameName.COLLECTOR, new File(outDir, names.file("framenames")), names.table("framenames"), names.columns("framenames"));
			Insert.insert(FrameSubName.COLLECTOR, new File(outDir, names.file("framesubnames")), names.table("framesubnames"), names.columns("framesubnames"));
			Insert.insert(FrameExample.COLLECTOR, new File(outDir, names.file("examples")), names.table("examples"), names.columns("examples"));
			Insert.insert(Syntax.COLLECTOR, new File(outDir, names.file("syntaxes")), names.table("syntaxes"), names.columns("syntaxes"));
			Insert.insert(Semantics.COLLECTOR, new File(outDir, names.file("semantics")), names.table("semantics"), names.columns("semantics"));
			Insert.insert(Predicate.COLLECTOR, new File(outDir, names.file("predicates")), names.table("predicates"), names.columns("predicates"));

			Insert.insert(Role.COLLECTOR, new File(outDir, names.file("roles")), names.table("roles"), names.columns("roles"));
			Insert.insert(Frame.COLLECTOR, new File(outDir, names.file("frames")), names.table("frames"), names.columns("frames"));

			Insert.insert(Frame_Example.SET, null, new File(outDir, names.file("frames_examples")), names.table("frames_examples"), names.columns("frames_examples"));
			Insert.insert(Predicate_Semantics.SET, null, new File(outDir, names.file("predicates_semantics")), names.table("predicates_semantics"), names.columns("predicates_semantics"));

			Insert.insert(Class_Word.SET, Class_Word.COMPARATOR, new File(outDir, names.file("members")), names.table("members"), names.columns("members"));
			Insert.insert(Class_Role.SET, Class_Role.COMPARATOR, new File(outDir, names.file("classes_roles")), names.table("classes_roles"), names.columns("classes_roles"));
			Insert.insert(Class_Frame.SET, Class_Frame.COMPARATOR, new File(outDir, names.file("classes_frames")), names.table("classes_frames"), names.columns("classes_frames"));
			Insert.insert(Member_Grouping.SET, Member_Grouping.COMPARATOR, new File(outDir, names.file("members_groupings")), names.table("members_groupings"), names.columns("members_groupings"));
			Insert.insert(Member_Sense.SET, Member_Sense.COMPARATOR, new File(outDir, names.file("members_senses")), names.table("members_senses"), names.columns("members_senses"));

			// R E S O L V A B L E
			insertWords();
		}
	}

	protected void insertWords() throws FileNotFoundException
	{
		Insert.insert(Word.COLLECTOR, new File(outDir, names.file("words")), names.table("words"), names.columns("words"));
	}
}
