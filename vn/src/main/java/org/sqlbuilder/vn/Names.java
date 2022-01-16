package org.sqlbuilder.vn;

public class Names
{
	static public class CLASSES
	{
		public static final String FILE = "classes.sql";
		public static final String TABLE = "vn_classes";
		public static final String COLUMNS = "classid,class,classtag";
	}

	static public class MEMBERS // CLASSES_WORDS
	{
		public static final String FILE = "members.sql";
		public static final String TABLE = "vn_members";
		public static final String COLUMNS = "classid,vnwordid";
	}

	static public class MEMBERS_SENSES
	{
		public static final String FILE = "members_senses.sql";
		public static final String TABLE = "vn_members_senses";
		public static final String COLUMNS = "classid,vnwordid,sensenum,synsetid,sensekey,quality";
	}

	static public class GROUPINGS
	{
		public static final String FILE = "groupings.sql";
		public static final String TABLE = "vn_groupings";
		public static final String COLUMNS = "groupingid,grouping";
	}

	static public class MEMBERS_GROUPINGS
	{
		public static final String FILE = "members_groupings.sql";
		public static final String TABLE = "vn_members_groupings";
		public static final String COLUMNS = "groupingmapid,classid,vnwordid,groupingid";
	}

	static public class ROLETYPES
	{
		public static final String FILE = "roletypes.sql";
		public static final String TABLE = "vn_roletypes";
		public static final String COLUMNS = "roletypeid,roletype";
	}

	static public class ROLES
	{
		public static final String FILE = "roles.sql";
		public static final String TABLE = "vn_roles";
		public static final String COLUMNS = "roleid,roletypeid,restrsid";
	}

	static public class ROLES_CLASSES
	{
		public static final String FILE = "roles_classes.sql";
		public static final String TABLE = "vn_roles_classes";
		public static final String COLUMNS = "rolemapid,roleid`,classid";
	}

	static public class RESTRTYPES
	{
		public static final String FILE = "restrtypes.sql";
		public static final String TABLE = "vn_restrtypes";
		public static final String COLUMNS = "restrid,restrval,issyn";
	}

	static public class RESTRS
	{
		public static final String FILE = "restrs.sql";
		public static final String TABLE = "vn_restrs";
		public static final String COLUMNS = "restrsid,restrs,issyn";
	}

	static public class FRAMES
	{
		public static final String FILE = "frames.sql";
		public static final String TABLE = "vn_frames";
		public static final String COLUMNS = "frameid,number,xtag,nameid,subnameid,syntaxid,semanticsid";
	}

	static public class FRAMENAMES
	{
		public static final String FILE = "framenames.sql";
		public static final String TABLE = "vn_framenames";
		public static final String COLUMNS = "nameid, framename";
	}

	static public class FRAMESUBNAMES
	{
		public static final String FILE = "framesubnames.sql";
		public static final String TABLE = "vn_framesubnames";
		public static final String COLUMNS = "subnameid,framesubname";
	}

	static public class CLASSES_FRAMES
	{
		public static final String FILE = "clesses_frames.sql";
		public static final String TABLE = "vn_classes_frames";
		public static final String COLUMNS = "framemapid,classid,frameid";
	}

	static public class EXAMPLES
	{
		public static final String FILE = "examples.sql";
		public static final String TABLE = "vn_examples";
		public static final String COLUMNS = "exampleid,example";
	}

	static public class FRAMES_EXAMPLES
	{
		public static final String FILE = "frames_examples.sql";
		public static final String TABLE = "vn_frames_examples";
		public static final String COLUMNS = "frameid,exampleid";
	}

	static public class SEMANTICS
	{
		public static final String FILE = "semantics.sql";
		public static final String TABLE = "vn_semantics";
		public static final String COLUMNS = "semanticsid,semantics";
	}

	static public class PREDICATES
	{
		public static final String FILE = "predicates.sql";
		public static final String TABLE = "vn_predicates";
		public static final String COLUMNS = "predid,pred";
	}

	static public class SEMANTICS_PREDICATES
	{
		public static final String FILE = "semantics_predicates.sql";
		public static final String TABLE = "vn_semantics_predicates";
		public static final String COLUMNS = "semanticsid,predid";
	}

	static public class SYNTAXES
	{
		public static final String FILE = "syntaxes.sql";
		public static final String TABLE = "vn_syntaxes";
		public static final String COLUMNS = "syntaxid,syntax";
	}

	static public class WORDS
	{
		public static final String FILE = "words.sql";
		public static final String TABLE = "vn_words";
		public static final String COLUMNS = "vnwordid,wordid,lemma";
	}
}
