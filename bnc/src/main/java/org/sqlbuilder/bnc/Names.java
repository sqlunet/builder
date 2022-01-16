package org.sqlbuilder.bnc;

public class Names
{
	static public class BNC
	{
		public static final String FILE = "bnc.sql";
		public static final String TABLE = "bnc";
		public static final String COLUMNS = "wordid,word,pos,freq,range,disp";
	}

	static class BNC_Extended
	{
		public static final String COLUMNS = "wordid,word,posid,freq1,range1,disp1,freq2,range2,disp2,ll";
	}

	static public class BNC_SPWR extends BNC_Extended
	{
		public static final String FILE = "bncspwr.sql";
		public static final String TABLE = "bncspwr";
	}

	static public class BNC_CONVTASK extends BNC_Extended
	{
		public static final String FILE = "bncconvtask.sql";
		public static final String TABLE = "bncconvtask";
	}

	static public class BNC_IMAGINF extends BNC_Extended
	{
		public static final String FILE = "bncimaginf.sql";
		public static final String TABLE = "bncimaginf";
	}
}
