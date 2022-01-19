package org.sqlbuilder.fn;

public class Names
{
	static public class FRAMES
	{
		public static final String FILE = "frames.sql";
		public static final String TABLE = "fn_frames";
		public static final String COLUMNS = "frameid,name,definition";
	}

	static public class FES
	{
		public static final String FILE = "fes.sql";
		public static final String TABLE = "fn_fes";
		public static final String COLUMNS = "feid,frameid,name,abbrev,definition,coretype,coreset";
	}

	static public class WORDS
	{
		public static final String FILE = "fnwords.sql";
		public static final String TABLE = "fn_words";
		public static final String COLUMNS = "fnwordid,wordid,word";
	}
}
