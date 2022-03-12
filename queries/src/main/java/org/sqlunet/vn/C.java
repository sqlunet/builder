/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>.
 */

package org.sqlunet.vn;

/**
 * VerbNet provider contract
 *
 * @author <a href="mailto:1313ou@gmail.com">Bernard Bou</a>
 */
public class C
{
	static public final class VnClasses
	{
		static public final String TABLE = "vnclasses";
		static public final String CONTENT_URI_TABLE = VnClasses.TABLE;
		static public final String WORDID = "wordid";
		static public final String POS = "pos";
		static public final String CLASSID = "classid";
		static public final String CLASS = "class";
		static public final String CLASSTAG = "classtag";
	}

	static public final class VnWords
	{
		static public final String TABLE = "vnwords";
		static public final String CONTENT_URI_TABLE = VnWords.TABLE;
		static public final String VNWORDID = "vnwordid";
		static public final String WORDID = "wordid";
		static public final String WORD = "word";
	}

	static public final class VnMembers
	{
		static public final String TABLE = "vnmembers";
		static public final String CONTENT_URI_TABLE = VnMembers.TABLE;
		static public final String CLASSID = "classid";
		static public final String VNWORDID = "vnwordid";
	}

	static public final class VnGroupings
	{
		static public final String TABLE = "vngroupings";
		static public final String CONTENT_URI_TABLE = VnGroupings.TABLE;
		static public final String GROUPINGID = "groupingid";
		static public final String GROUPING = "grouping";
	}

	static public final class VnMembers_VnGroupings
	{
		static public final String TABLE = "vnmembers_vngroupings";
		static public final String CONTENT_URI_TABLE = VnMembers_VnGroupings.TABLE;
		static public final String CLASSID = "classid";
		static public final String VNWORDID = "vnwordid";
		static public final String GROUPINGID = "groupingid";
	}

	static public final class VnMembers_Senses
	{
		static public final String TABLE = "vnmembers_senses";
		static public final String CONTENT_URI_TABLE = VnMembers_Senses.TABLE;
		static public final String CLASSID = "classid";
		static public final String VNWORDID = "vnwordid";
		static public final String SENSENUM = "sensenum";
		static public final String SENSEKEY = "sensekey";
		static public final String WORDID = "wordid";
		static public final String SYNSETID = "synsetid";
		static public final String QUALITY = "quality";
	}

	static public final class VnRoles
	{
		static public final String TABLE = "vnroles";
		static public final String CONTENT_URI_TABLE = VnRoles.TABLE;
		static public final String ROLEID = "roleid";
	}

	static public final class VnRoleTypes
	{
		static public final String TABLE = "vnroletypes";
		static public final String CONTENT_URI_TABLE = VnRoles.TABLE;
		static public final String ROLETYPEID = "roletypeid";
	}

	static public final class VnClasses_VnRoles
	{
		static public final String TABLE = "vnclasses_vnroles";
		static public final String CONTENT_URI_TABLE = VnClasses_VnRoles.TABLE;
		static public final String CLASSID = "classid";
		static public final String ROLEID = "roleid";
		static public final String ROLETYPE = "roletype";
		static public final String RESTRS = "restrs";
	}

	static public final class VnFrames
	{
		static public final String TABLE = "vnframes";
		static public final String CONTENT_URI_TABLE = VnFrames.TABLE;
		static public final String FRAMEID = "frameid";
		static public final String FRAMENAMEID = "framenameid";
		static public final String FRAMESUBNAMEID = "framesubnameid";
		static public final String SYNTAXID = "syntaxid";
		static public final String SEMANTICSID = "semanticsid";
	}

	static public final class VnFrameNames
	{
		static public final String TABLE = "vnframenames";
		static public final String CONTENT_URI_TABLE = VnFrameNames.TABLE;
		static public final String FRAMENAMEID = "nameid";
	}

	static public final class VnFrameSubNames
	{
		static public final String TABLE = "vnframesubnames";
		static public final String CONTENT_URI_TABLE = VnFrameSubNames.TABLE;
		static public final String FRAMESUBNAMEID = "subnameid";
	}

	static public final class VnFrames_VnExamples
	{
		static public final String TABLE = "vnframes_examples";
		static public final String CONTENT_URI_TABLE = VnFrames_VnExamples.TABLE;
		static public final String FRAMEID = "frameid";
		static public final String EXAMPLEID = "exampleid";
	}

	static public final class VnExamples
	{
		static public final String TABLE = "vnexamples";
		static public final String CONTENT_URI_TABLE = VnExamples.TABLE;
		static public final String EXAMPLEID = "exampleid";
		static public final String EXAMPLE = "example";
	}

	static public final class VnSyntaxes
	{
		static public final String TABLE = "vnsyntaxes";
		static public final String CONTENT_URI_TABLE = VnSyntaxes.TABLE;
		static public final String SYNTAXID = "syntaxid";
		static public final String SYNTAX = "syntax";
	}

	static public final class VnSemantics
	{
		static public final String TABLE = "vnsemantics";
		static public final String CONTENT_URI_TABLE = VnSyntaxes.TABLE;
		static public final String SEMANTICSID = "semanticsid";
		static public final String SEMANTICS = "semantics";
	}

	static public final class VnClasses_VnFrames
	{
		static public final String TABLE = "vnclasses_vnframes";
		static public final String CONTENT_URI_TABLE = VnClasses_VnFrames.TABLE;
		static public final String CLASSID = "classid";
		static public final String FRAMEID = "frameid";
	}

	static public final class VnRestrs
	{
		static public final String TABLE = "vnrestrs";
		static public final String CONTENT_URI_TABLE = VnRestrs.TABLE;
		static public final String RESTRSID = "restrsid";
	}

	static public final class VnRestrTypes
	{
		static public final String TABLE = "restrtypes";
		static public final String CONTENT_URI_TABLE = VnRestrTypes.TABLE;
		static public final String RESTRTYPEID = "restrtypeid";
	}

	static public final class Lookup_VnExamples
	{
		static public final String TABLE = "fts_vnexamples";
		static public final String CONTENT_URI_TABLE = Lookup_VnExamples.TABLE;
		static public final String EXAMPLEID = "exampleid";
		static public final String EXAMPLE = "example";
		static public final String CLASSID = "classid";
		static public final String FRAMEID = "frameid";
	}

	static public final class Suggest_VnWords
	{
		static public final String VNWORDID = "vnwordid";
		static public final String WORDID = "wordid";
		static public final String WORD = "word";
		static final String SEARCH_WORD_PATH = "suggest_vnword";
		static public final String TABLE = Suggest_VnWords.SEARCH_WORD_PATH + "/" + "SearchManager.SUGGEST_URI_PATH_QUERY";
	}

	static public final class Suggest_FTS_VnWords
	{
		static public final String VNWORDID = "vnwordid";
		static public final String WORDID = "wordid";
		static public final String WORD = "word";
		static final String SEARCH_WORD_PATH = "suggest_fts_vnword";
		static public final String TABLE = Suggest_FTS_VnWords.SEARCH_WORD_PATH + "/" + "SearchManager.SUGGEST_URI_PATH_QUERY";
	}

	static public final class Lookup_VnExamples_X
	{
		static public final String TABLE = "fts_vnexamples_x";
		static public final String TABLE_BY_EXAMPLE = "fts_vnexamples_x_by_example";
		static public final String CONTENT_URI_TABLE = Lookup_VnExamples_X.TABLE_BY_EXAMPLE;
	}

	static public final class VnClasses_VnMembers_X
	{
		static public final String TABLE_BY_WORD = "vnclasses_vnmembers_x_by_word";
		static public final String CONTENT_URI_TABLE = VnClasses_VnMembers_X.TABLE_BY_WORD;
	}

	static public final class VnClasses_VnRoles_X
	{
		static public final String TABLE_BY_ROLE = "vnclasses_vnroles_x_by_vnrole";
		static public final String CONTENT_URI_TABLE = VnClasses_VnRoles_X.TABLE_BY_ROLE;
	}

	static public final class VnClasses_VnFrames_X
	{
		static public final String TABLE_BY_FRAME = "vnclasses_vnframes_x_by_vnframe";
		static public final String CONTENT_URI_TABLE = VnClasses_VnFrames_X.TABLE_BY_FRAME;
	}

	static public final class Words_VnClasses
	{
		static public final String TABLE = "words_vnclasses";
		static public final String CONTENT_URI_TABLE = Words_VnClasses.TABLE;
		static public final String WORDID = "wordid";
		static public final String SYNSETID = "synsetid";
		static public final String CLASSID = "classid";
		static public final String CLASS = "class";
		static public final String CLASSTAG = "classtag";
		static public final String SENSENUM = "sensenum";
		static public final String SENSEKEY = "sensekey";
		static public final String QUALITY = "quality";
		static public final String NULLSYNSET = "nullsynset";
	}

	static public final class Words
	{
		static public final String TABLE = "words";
		static public final String CONTENT_URI_TABLE = Words.TABLE;
		static public final String WORDID = "wordid";
		static public final String WORD = "word";
	}

	static public final class Synsets
	{
		static public final String TABLE = "synsets";
		static public final String CONTENT_URI_TABLE = Synsets.TABLE;
		static public final String SYNSETID = "synsetid";
		static public final String DEFINITION = "definition";
	}
}
