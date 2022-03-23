package org.sqlunet.vn;
public class V {
static public class Synsets {
static public final String TABLE = "${wnsynsets.table}";
static public final String SYNSETID = "${wnsynsets.synsetid}";
static public final String DEFINITION = "${wnsynsets.definition}";
}
static public class Words {
static public final String TABLE = "${wnwords.table}";
static public final String WORDID = "${wnwords.wordid}";
static public final String WORD = "${wnwords.word}";
}
static public class Words_VnClasses {
static public final String TABLE = "${wnwords-classes.table}";
static public final String WORDID = "${wnwords-classes.wordid}";
static public final String SYNSETID = "${wnwords-classes.synsetid}";
static public final String CLASSID = "${wnwords-classes.classid}";
static public final String CLASS = "${wnwords-classes.class}";
static public final String CLASSTAG = "${wnwords-classes.classtag}";
static public final String SENSENUM = "${wnwords-classes.sensenum}";
static public final String SENSEKEY = "${wnwords-classes.sensekey}";
static public final String QUALITY = "${wnwords-classes.quality}";
static public final String NULLSYNSET = "${wnwords-classes.nullsynset}";
}
static public class VnClasses_VnFrames_X {
static public final String TABLE_BY_FRAME = "${classes-frames_x.table_by_frame}";
}
static public class VnClasses_VnRoles_X {
static public final String TABLE_BY_ROLE = "${classes-roles_x.table_by_role}";
}
static public class VnClasses_VnMembers_X {
static public final String TABLE_BY_WORD = "${classes-members_x.table_by_word}";
}
static public class Lookup_VnExamples_X {
static public final String TABLE = "${lookup-examples_x.table}";
static public final String TABLE_BY_EXAMPLE = "${lookup-examples_x.table_by_example}";
}
static public class Suggest_FTS_VnWords {
static public final String VNWORDID = "${suggest_fts-words.vnwordid}";
static public final String WORDID = "${suggest_fts-words.wordid}";
static public final String WORD = "${suggest_fts-words.word}";
static public final String SEARCH_WORD_PATH = "${suggest_fts-words.search_word_path}";
static public final String TABLE = "${suggest_fts-words.table}";
}
static public class Suggest_VnWords {
static public final String VNWORDID = "${suggest-words.vnwordid}";
static public final String WORDID = "${suggest-words.wordid}";
static public final String WORD = "${suggest-words.word}";
static public final String SEARCH_WORD_PATH = "${suggest-words.search_word_path}";
static public final String TABLE = "${suggest-words.table}";
}
static public class Lookup_VnExamples {
static public final String TABLE = "${lookup-examples.table}";
static public final String EXAMPLEID = "${lookup-examples.exampleid}";
static public final String EXAMPLE = "${lookup-examples.example}";
static public final String CLASSID = "${lookup-examples.classid}";
static public final String FRAMEID = "${lookup-examples.frameid}";
}
static public class VnRestrTypes {
static public final String TABLE = "${restrtypes.table}";
static public final String RESTRTYPEID = "${restrtypes.restrtypeid}";
}
static public class VnRestrs {
static public final String TABLE = "${restrs.table}";
static public final String RESTRSID = "${restrs.restrsid}";
}
static public class VnClasses_VnFrames {
static public final String TABLE = "${classes-frames.table}";
static public final String CLASSID = "${classes-frames.classid}";
static public final String FRAMEID = "${classes-frames.frameid}";
}
static public class VnSemantics {
static public final String TABLE = "${semantics.table}";
static public final String SEMANTICSID = "${semantics.semanticsid}";
static public final String SEMANTICS = "${semantics.semantics}";
}
static public class VnSyntaxes {
static public final String TABLE = "${syntaxes.table}";
static public final String SYNTAXID = "${syntaxes.syntaxid}";
static public final String SYNTAX = "${syntaxes.syntax}";
}
static public class VnExamples {
static public final String TABLE = "${examples.table}";
static public final String EXAMPLEID = "${examples.exampleid}";
static public final String EXAMPLE = "${examples.example}";
}
static public class VnFrames_VnExamples {
static public final String TABLE = "${frames-examples.table}";
static public final String FRAMEID = "${frames-examples.frameid}";
static public final String EXAMPLEID = "${frames-examples.exampleid}";
}
static public class VnFrameSubNames {
static public final String TABLE = "${framesubnames.table}";
static public final String FRAMESUBNAMEID = "${framesubnames.framesubnameid}";
}
static public class VnFrameNames {
static public final String TABLE = "${framenames.table}";
static public final String FRAMENAMEID = "${framenames.framenameid}";
}
static public class VnFrames {
static public final String TABLE = "${frames.table}";
static public final String FRAMEID = "${frames.frameid}";
static public final String FRAMENAMEID = "${frames.framenameid}";
static public final String FRAMESUBNAMEID = "${frames.framesubnameid}";
static public final String SYNTAXID = "${frames.syntaxid}";
static public final String SEMANTICSID = "${frames.semanticsid}";
}
static public class VnClasses_VnRoles {
static public final String TABLE = "${classes-roles.table}";
static public final String CLASSID = "${classes-roles.classid}";
static public final String ROLEID = "${classes-roles.roleid}";
static public final String ROLETYPE = "${classes-roles.roletype}";
static public final String RESTRS = "${classes-roles.restrs}";
}
static public class VnRoleTypes {
static public final String TABLE = "${roletypes.table}";
static public final String ROLETYPEID = "${roletypes.roletypeid}";
}
static public class VnRoles {
static public final String TABLE = "${roles.table}";
static public final String ROLEID = "${roles.roleid}";
}
static public class VnMembers_Senses {
static public final String TABLE = "${members_senses.table}";
static public final String CLASSID = "${members_senses.classid}";
static public final String VNWORDID = "${members_senses.vnwordid}";
static public final String SENSENUM = "${members_senses.sensenum}";
static public final String SENSEKEY = "${members_senses.sensekey}";
static public final String WORDID = "${members_senses.wordid}";
static public final String SYNSETID = "${members_senses.synsetid}";
static public final String QUALITY = "${members_senses.quality}";
}
static public class VnMembers_VnGroupings {
static public final String TABLE = "${members-groupings.table}";
static public final String CLASSID = "${members-groupings.classid}";
static public final String VNWORDID = "${members-groupings.vnwordid}";
static public final String GROUPINGID = "${members-groupings.groupingid}";
}
static public class VnGroupings {
static public final String TABLE = "${groupings.table}";
static public final String GROUPINGID = "${groupings.groupingid}";
static public final String GROUPING = "${groupings.grouping}";
}
static public class VnMembers {
static public final String TABLE = "${members.table}";
static public final String CLASSID = "${members.classid}";
static public final String VNWORDID = "${members.vnwordid}";
}
static public class VnWords {
static public final String TABLE = "${words.table}";
static public final String VNWORDID = "${words.vnwordid}";
static public final String WORDID = "${words.wordid}";
static public final String WORD = "${words.word}";
}
static public class VnClasses {
static public final String TABLE = "${classes.table}";
static public final String WORDID = "${classes.wordid}";
static public final String POS = "${classes.pos}";
static public final String CLASSID = "${classes.classid}";
static public final String CLASS = "${classes.class}";
static public final String CLASSTAG = "${classes.classtag}";
}
}
