package org.sqlunet.vn;
public class V {
static public class Synsets {
static public final String TABLE = "${synsets.table}";
static public final String SYNSETID = "${synsets.synsetid}";
static public final String DEFINITION = "${synsets.definition}";
}
static public class Words {
static public final String TABLE = "${words.table}";
static public final String WORDID = "${words.wordid}";
static public final String WORD = "${words.word}";
}
static public class Words_VnClasses {
static public final String TABLE = "${words_vnclasses.table}";
static public final String WORDID = "${words_vnclasses.wordid}";
static public final String SYNSETID = "${words_vnclasses.synsetid}";
static public final String CLASSID = "${words_vnclasses.classid}";
static public final String CLASS = "${words_vnclasses.class}";
static public final String CLASSTAG = "${words_vnclasses.classtag}";
static public final String SENSENUM = "${words_vnclasses.sensenum}";
static public final String SENSEKEY = "${words_vnclasses.sensekey}";
static public final String QUALITY = "${words_vnclasses.quality}";
static public final String NULLSYNSET = "${words_vnclasses.nullsynset}";
}
static public class VnClasses_VnFrames_X {
static public final String TABLE_BY_FRAME = "${vnclasses_vnframes_x.table_by_frame}";
}
static public class VnClasses_VnRoles_X {
static public final String TABLE_BY_ROLE = "${vnclasses_vnroles_x.table_by_role}";
}
static public class VnClasses_VnMembers_X {
static public final String TABLE_BY_WORD = "${vnclasses_vnmembers_x.table_by_word}";
}
static public class Lookup_VnExamples_X {
static public final String TABLE = "${lookup_vnexamples_x.table}";
static public final String TABLE_BY_EXAMPLE = "${lookup_vnexamples_x.table_by_example}";
}
static public class Suggest_FTS_VnWords {
static public final String VNWORDID = "${suggest_fts_vnwords.vnwordid}";
static public final String WORDID = "${suggest_fts_vnwords.wordid}";
static public final String WORD = "${suggest_fts_vnwords.word}";
static public final String SEARCH_WORD_PATH = "${suggest_fts_vnwords.search_word_path}";
static public final String TABLE = "${suggest_fts_vnwords.table}";
}
static public class Suggest_VnWords {
static public final String VNWORDID = "${suggest_vnwords.vnwordid}";
static public final String WORDID = "${suggest_vnwords.wordid}";
static public final String WORD = "${suggest_vnwords.word}";
static public final String SEARCH_WORD_PATH = "${suggest_vnwords.search_word_path}";
static public final String TABLE = "${suggest_vnwords.table}";
}
static public class Lookup_VnExamples {
static public final String TABLE = "${lookup_vnexamples.table}";
static public final String EXAMPLEID = "${lookup_vnexamples.exampleid}";
static public final String EXAMPLE = "${lookup_vnexamples.example}";
static public final String CLASSID = "${lookup_vnexamples.classid}";
static public final String FRAMEID = "${lookup_vnexamples.frameid}";
}
static public class VnRestrTypes {
static public final String TABLE = "${vnrestrtypes.table}";
static public final String RESTRTYPEID = "${vnrestrtypes.restrtypeid}";
}
static public class VnRestrs {
static public final String TABLE = "${vnrestrs.table}";
static public final String RESTRSID = "${vnrestrs.restrsid}";
}
static public class VnClasses_VnFrames {
static public final String TABLE = "${vnclasses_vnframes.table}";
static public final String CLASSID = "${vnclasses_vnframes.classid}";
static public final String FRAMEID = "${vnclasses_vnframes.frameid}";
}
static public class VnSemantics {
static public final String TABLE = "${vnsemantics.table}";
static public final String SEMANTICSID = "${vnsemantics.semanticsid}";
static public final String SEMANTICS = "${vnsemantics.semantics}";
}
static public class VnSyntaxes {
static public final String TABLE = "${vnsyntaxes.table}";
static public final String SYNTAXID = "${vnsyntaxes.syntaxid}";
static public final String SYNTAX = "${vnsyntaxes.syntax}";
}
static public class VnExamples {
static public final String TABLE = "${vnexamples.table}";
static public final String EXAMPLEID = "${vnexamples.exampleid}";
static public final String EXAMPLE = "${vnexamples.example}";
}
static public class VnFrames_VnExamples {
static public final String TABLE = "${vnframes_vnexamples.table}";
static public final String FRAMEID = "${vnframes_vnexamples.frameid}";
static public final String EXAMPLEID = "${vnframes_vnexamples.exampleid}";
}
static public class VnFrameSubNames {
static public final String TABLE = "${vnframesubnames.table}";
static public final String FRAMESUBNAMEID = "${vnframesubnames.framesubnameid}";
}
static public class VnFrameNames {
static public final String TABLE = "${vnframenames.table}";
static public final String FRAMENAMEID = "${vnframenames.framenameid}";
}
static public class VnFrames {
static public final String TABLE = "${vnframes.table}";
static public final String FRAMEID = "${vnframes.frameid}";
static public final String FRAMENAMEID = "${vnframes.framenameid}";
static public final String FRAMESUBNAMEID = "${vnframes.framesubnameid}";
static public final String SYNTAXID = "${vnframes.syntaxid}";
static public final String SEMANTICSID = "${vnframes.semanticsid}";
}
static public class VnClasses_VnRoles {
static public final String TABLE = "${vnclasses_vnroles.table}";
static public final String CLASSID = "${vnclasses_vnroles.classid}";
static public final String ROLEID = "${vnclasses_vnroles.roleid}";
static public final String ROLETYPE = "${vnclasses_vnroles.roletype}";
static public final String RESTRS = "${vnclasses_vnroles.restrs}";
}
static public class VnRoleTypes {
static public final String TABLE = "${vnroletypes.table}";
static public final String ROLETYPEID = "${vnroletypes.roletypeid}";
}
static public class VnRoles {
static public final String TABLE = "${vnroles.table}";
static public final String ROLEID = "${vnroles.roleid}";
}
static public class VnMembers_Senses {
static public final String TABLE = "${vnmembers_senses.table}";
static public final String CLASSID = "${vnmembers_senses.classid}";
static public final String VNWORDID = "${vnmembers_senses.vnwordid}";
static public final String SENSENUM = "${vnmembers_senses.sensenum}";
static public final String SENSEKEY = "${vnmembers_senses.sensekey}";
static public final String WORDID = "${vnmembers_senses.wordid}";
static public final String SYNSETID = "${vnmembers_senses.synsetid}";
static public final String QUALITY = "${vnmembers_senses.quality}";
}
static public class VnMembers_VnGroupings {
static public final String TABLE = "${vnmembers_vngroupings.table}";
static public final String CLASSID = "${vnmembers_vngroupings.classid}";
static public final String VNWORDID = "${vnmembers_vngroupings.vnwordid}";
static public final String GROUPINGID = "${vnmembers_vngroupings.groupingid}";
}
static public class VnGroupings {
static public final String TABLE = "${vngroupings.table}";
static public final String GROUPINGID = "${vngroupings.groupingid}";
static public final String GROUPING = "${vngroupings.grouping}";
}
static public class VnMembers {
static public final String TABLE = "${vnmembers.table}";
static public final String CLASSID = "${vnmembers.classid}";
static public final String VNWORDID = "${vnmembers.vnwordid}";
}
static public class VnWords {
static public final String TABLE = "${vnwords.table}";
static public final String VNWORDID = "${vnwords.vnwordid}";
static public final String WORDID = "${vnwords.wordid}";
static public final String WORD = "${vnwords.word}";
}
static public class VnClasses {
static public final String TABLE = "${vnclasses.table}";
static public final String WORDID = "${vnclasses.wordid}";
static public final String POS = "${vnclasses.pos}";
static public final String CLASSID = "${vnclasses.classid}";
static public final String CLASS = "${vnclasses.class}";
static public final String CLASSTAG = "${vnclasses.classtag}";
}
}
