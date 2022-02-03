package org.sqlbuilder.pb;

import org.sqlbuilder.common.Insertable;

public class PbAlias implements Insertable
{
	enum Db
	{
		VERBNET, FRAMENET
	}

	private final String lemma;

	private final String ref;

	private final String pos;

	private final Db db;

	private PbRoleSet pbRoleSet;

	private PbWord pbWord;

	public PbAlias(final Db db, final String clazz, final String pos, final String lemma, final PbRoleSet pbRoleSet, final PbWord pbWord)
	{
		this.ref = clazz;
		this.pos = "j".equals(pos) ? "a" : pos;
		this.db = db;
		this.lemma = lemma;
		this.pbRoleSet = pbRoleSet;
		this.pbWord = pbWord;
	}

	public PbRoleSet getPbRoleSet()
	{
		return pbRoleSet;
	}

	public void setPbRoleSet(final PbRoleSet pbRoleSet)
	{
		this.pbRoleSet = pbRoleSet;
	}

	public PbWord getPbWord()
	{
		return pbWord;
	}

	public void setPbWord(final PbWord pbWord)
	{
		this.pbWord = pbWord;
	}

	@Override
	public String dataRow()
	{
		// String sql = null;
		// String table = null;
		// long refId = 0;
		// try
		// {
		// 	switch (this.db)
		// 	{
		// 		case VERBNET:
		// 			sql = PbAlias.SQL_INSERT_VN;
		// 			table = PbAlias.TABLE_VN;
		// 			try
		// 			{
		// 				refId = PbVnFinder.findVnClassId(connection, this.ref);
		// 			}
		// 			catch (NotFoundException nfe)
		// 			{
		// 				refId = PbVnFinder.findVnClassIdFromClassTag(connection, this.ref);
		// 			}
		// 			break;
		//
		// 		case FRAMENET:
		// 			sql = PbAlias.SQL_INSERT_FN;
		// 			table = PbAlias.TABLE_FN;
		// 			refId = PbFnFinder.findFnFrameId(connection, this.ref);
		// 			break;
		//
		// 		default:
		// 			break;
		// 	}
		// }
		// catch (SQLException sqle)
		// {
		// 	throw new NotFoundException(this.ref, sqle);
		// }

		// Long(1, this.pbRoleSet);
		// Long(2, refId);
		// String(3, this.pos);
		// Long(4, this.pbWord);
		return null;
	}

	@Override
	public String toString()
	{
		return this.db + ":" + this.ref + "(" + this.pos + ")-" + this.lemma;
	}
}
