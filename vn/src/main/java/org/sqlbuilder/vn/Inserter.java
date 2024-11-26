package org.sqlbuilder.vn;

import org.sqlbuilder.annotations.ProvidesIdTo;
import org.sqlbuilder.common.Insert;
import org.sqlbuilder.common.Names;
import org.sqlbuilder.common.Progress;
import org.sqlbuilder.vn.joins.*;
import org.sqlbuilder.vn.objects.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.Properties;

public class Inserter
{
	protected final Names names;

	protected String header;

	protected File outDir;

	public Inserter(final Properties conf)
	{
		this.names = new Names("vn");
		this.header = conf.getProperty("vn_header");
		this.outDir = new File(conf.getProperty("vn_outdir", "sql/data"));
		if (!this.outDir.exists())
		{
			//noinspection ResultOfMethodCallIgnored
			this.outDir.mkdirs();
		}
	}

	public void insert() throws FileNotFoundException
	{
		try ( //
		      @ProvidesIdTo(type = VnClass.class) var ignored10 = VnClass.COLLECTOR.open(); //
		      @ProvidesIdTo(type = RoleType.class) var ignored20 = RoleType.COLLECTOR.open(); //
		      @ProvidesIdTo(type = Role.class) var ignored21 = Role.COLLECTOR.open(); //
		      @ProvidesIdTo(type = RestrType.class) var ignored22 = RestrType.COLLECTOR.open(); //
		      @ProvidesIdTo(type = Restrs.class) var ignored23 = Restrs.COLLECTOR.open(); //
		      @ProvidesIdTo(type = Frame.class) var ignored30 = Frame.COLLECTOR.open(); //
		      @ProvidesIdTo(type = FrameName.class) var ignored31 = FrameName.COLLECTOR.open(); //
		      @ProvidesIdTo(type = FrameSubName.class) var ignored32 = FrameSubName.COLLECTOR.open(); //
		      @ProvidesIdTo(type = FrameExample.class) var ignored33 = FrameExample.COLLECTOR.open(); //
		      @ProvidesIdTo(type = Syntax.class) var ignored34 = Syntax.COLLECTOR.open(); //
		      @ProvidesIdTo(type = Semantics.class) var ignored35 = Semantics.COLLECTOR.open(); //
		      @ProvidesIdTo(type = Grouping.class) var ignored40 = Grouping.COLLECTOR.open(); //
		      @ProvidesIdTo(type = Predicate.class) var ignored41 = Predicate.COLLECTOR.open(); //
		      @ProvidesIdTo(type = Word.class) var ignored50 = Word.COLLECTOR.open() //
		)
		{
			Progress.tracePending("collector", "class");
			Insert.insert(VnClass.COLLECTOR, VnClass.COLLECTOR, new File(outDir, names.file("classes")), names.table("classes"), names.columns("classes"), header);
			Progress.traceDone();

			Progress.tracePending("collector", "roletype");
			Insert.insert(RoleType.COLLECTOR, RoleType.COLLECTOR, new File(outDir, names.file("roletypes")), names.table("roletypes"), names.columns("roletypes"), header);
			Progress.traceDone();

			Progress.tracePending("collector", "role");
			Insert.insert(Role.COLLECTOR, Role.COLLECTOR, new File(outDir, names.file("roles")), names.table("roles"), names.columns("roles"), header);
			Progress.traceDone();

			Progress.tracePending("collector", "restrtype");
			Insert.insert(RestrType.COLLECTOR, RestrType.COLLECTOR, new File(outDir, names.file("restrtypes")), names.table("restrtypes"), names.columns("restrtypes"), header);
			Progress.traceDone();

			Progress.tracePending("collector", "restrs");
			Insert.insert(Restrs.COLLECTOR, Restrs.COLLECTOR, new File(outDir, names.file("restrs")), names.table("restrs"), names.columns("restrs"), header);
			Progress.traceDone();

			Progress.tracePending("collector", "name");
			Insert.insert(FrameName.COLLECTOR, FrameName.COLLECTOR, new File(outDir, names.file("framenames")), names.table("framenames"), names.columns("framenames"), header);
			Progress.traceDone();

			Progress.tracePending("collector", "subname");
			Insert.insert(FrameSubName.COLLECTOR, FrameSubName.COLLECTOR, new File(outDir, names.file("framesubnames")), names.table("framesubnames"), names.columns("framesubnames"), header);
			Progress.traceDone();

			Progress.tracePending("collector", "frame example");
			Insert.insert(FrameExample.COLLECTOR, FrameExample.COLLECTOR, new File(outDir, names.file("examples")), names.table("examples"), names.columns("examples"), header);
			Progress.traceDone();

			Progress.tracePending("collector", "syntax");
			Insert.insert(Syntax.COLLECTOR, Syntax.COLLECTOR, new File(outDir, names.file("syntaxes")), names.table("syntaxes"), names.columns("syntaxes"), header);
			Progress.traceDone();

			Progress.tracePending("collector", "semantics");
			Insert.insert(Semantics.COLLECTOR, Semantics.COLLECTOR, new File(outDir, names.file("semantics")), names.table("semantics"), names.columns("semantics"), header);
			Progress.traceDone();

			Progress.tracePending("collector", "predicate");
			Insert.insert(Predicate.COLLECTOR, Predicate.COLLECTOR, new File(outDir, names.file("predicates")), names.table("predicates"), names.columns("predicates"), header);
			Progress.traceDone();

			Progress.tracePending("collector", "frame");
			Insert.insert(Frame.COLLECTOR, Frame.COLLECTOR, new File(outDir, names.file("frames")), names.table("frames"), names.columns("frames"), header);
			Progress.traceDone();

			Progress.tracePending("set", "frame example");
			Insert.insert(Frame_Example.SET, (Comparator<Frame_Example>) null, new File(outDir, names.file("frames_examples")), names.table("frames_examples"), names.columns("frames_examples"), header);
			Progress.traceDone();

			Progress.tracePending("set", "predicate semantics");
			Insert.insert(Predicate_Semantics.SET, (Comparator<Predicate_Semantics>) null, new File(outDir, names.file("predicates_semantics")), names.table("predicates_semantics"), names.columns("predicates_semantics"), header);
			Progress.traceDone();

			Progress.tracePending("set", "class word");
			Insert.insert(Class_Word.SET, Class_Word.COMPARATOR, new File(outDir, names.file("members")), names.table("members"), names.columns("members"), header);
			Progress.traceDone();

			Progress.tracePending("set", "class frame");
			Insert.insert(Class_Frame.SET, Class_Frame.COMPARATOR, new File(outDir, names.file("classes_frames")), names.table("classes_frames"), names.columns("classes_frames"), header);
			Progress.traceDone();

			Progress.tracePending("collector", "grouping");
			Insert.insert(Grouping.COLLECTOR, Grouping.COLLECTOR, new File(outDir, names.file("groupings")), names.table("groupings"), names.columns("groupings"), header);
			Progress.traceDone();

			Progress.tracePending("set", "member grouping");
			Insert.insert(Member_Grouping.SET, Member_Grouping.COMPARATOR, new File(outDir, names.file("members_groupings")), names.table("members_groupings"), names.columns("members_groupings"), header);
			Progress.traceDone();

			// R E S O L V A B L E
			insertWords();
			insertMemberSenses();
		}
	}

	protected void insertWords() throws FileNotFoundException
	{
		Progress.tracePending("collector", "word");
		Insert.insert(Word.COLLECTOR, Word.COLLECTOR, new File(outDir, names.file("words")), names.table("words"), names.columns("words"), header);
		Progress.traceDone();
	}

	protected void insertMemberSenses() throws FileNotFoundException
	{
		Progress.tracePending("set", "member sense");
		Insert.insert(Member_Sense.SET, Member_Sense.COMPARATOR, new File(outDir, names.file("members_senses")), names.table("members_senses"), names.columns("members_senses"), header);
		Progress.traceDone();
	}
}
